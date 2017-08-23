
var map = L.map('mapid').setView([-36.914827,174.8072903], 13);
L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoiamFzb25saXV0YWljaGVuIiwiYSI6ImNqNmZ5ZGpkcjAzYWIzNXA1aWs5OXF3bXcifQ.33nis-JWUXYo1jpJkr1OSQ', {
		maxZoom: 18,
		attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, ' +
			'<a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
			'Imagery © <a href="http://mapbox.com">Mapbox</a>',
		id: 'mapbox.streets'
	}).addTo(map);
//alert(coordinates.length);

var polyline = L.polyline(coordinates, {color: 'red'}).addTo(map);
//var polyline = L.polyline(latlngs, {color: 'red'}).addTo(mymap);
// zoom the map to the polyline
map.fitBounds(polyline.getBounds());

 function display(data){
   console.log(data);
   alert(data);
   }

var geofences = new Object();
geofences.size = 0;



var drawnItems = L.geoJson().addTo(map);
map.addLayer(drawnItems);

var drawControl = new L.Control.Draw({
 draw: {
          polyline: false,
          rectangle: false,
          circle: false,
          marker: false,
          polygon: {
            allowIntersection: false
          }
        },
 edit:{
     featureGroup: drawnItems,
     edit:true,
 }
});

map.addControl(drawControl);


  map.on('draw:edited', function (e) {
      var layers = e.layers;
      layers.eachLayer(function (layer) {
          //do whatever you want; most likely save back to db
      });
  });
var isDetailDialogEnabled = true;

var count = 1;

   map.on('draw:created', function (e) {
     var type = e.layerType;
     var layer = e.layer;
     var feature = layer.feature = layer.feature || {};
     feature.type = feature.type || "Feature";
     var props = feature.properties = feature.properties || {};
                     //layer.feature = {properties: {}}; // No need to convert to GeoJSON.
     props.material = 'Top soil';
     props.zoneName = 'Zone '+ count++;
     props.zoneType = 'Loading zone';

      var latLngs = layer.getLatLngs()[0];
      var vertices = [];
      console.log(latLngs);
      console.log(latLngs.length);
      for(var i = 0; i < latLngs.length; i ++ ){
           vertices.push(new Object());
          vertices[i].latitude =  latLngs[i].lat;
          vertices[i].longitude =  latLngs[i].lng;
      }
      console.log(vertices);
        if( !geofences.hasOwnProperty("load")){
            var loadingFence = new Object();
            loadingFence.vertices = vertices;
            geofences.load = loadingFence;
            console.log("loading area specified");
        }else if( !geofences.hasOwnProperty("dump")){
             var dumpingFence = new Object();
            dumpingFence.vertices = vertices;
            geofences.dump = dumpingFence;
            console.log("dumping area specified");
        }
         //postLats(latLngs[0], display);

    drawnItems.addLayer(layer);
    map.addLayer(layer);
    layer.bindTooltip(props.zoneName,
              {permanent: true, direction:"center"}
             ).openTooltip()
    addPopup(layer);

   });


   function addPopup(layer) {
   layer.on('click', function() {
            if(isDetailDialogEnabled){
          $('#material').val(layer.feature.properties.material);
          $('#zoneName').val(layer.feature.properties.zoneName);
           $("input[name='zoneType'][value='" + layer.feature.properties.zoneType + "']").prop('checked', true);

           $('#okButton').on('click', function () {
           console.log("closing modal, saving details ");
            layer.feature.properties.material =  $('#material').val();
            layer.feature.properties.zoneName =  $('#zoneName').val();
            layer.feature.properties.zoneType =  $("input[name='zoneType']:checked").val();
              console.log( layer.feature.properties);
              if(layer.feature.properties.zoneType == 'Dumping zone'){
              console.log(layer);
                    layer.setStyle({fillColor:'#5FE828'});
              }
           })

          $('#zoneDetailDialog').modal({'show' : true, backdrop:'static', keyboard:false});
          }
           });

   }
map.on('draw:deletestart', function(event){
    isDetailDialogEnabled = false;
    console.log("on click function disabled");
});
map.on('draw:deletestop', function(event){
    isDetailDialogEnabled = true;
    console.log("on click function enabled");
});

function postGeofences(data){
postLats(data, display);
}

function postLats(lats, callbackfn){
    $.ajax({
       type: "POST",
       url: "/geofence",
       data: JSON.stringify(lats),
       contentType: "application/json; charset=utf-8",
       dataType: "json",


   }).then(function (data){
    console.log(data);
    var oldText =  $( "#result" ).text();
    var newText = oldText + data.content;
     $( "#result" ).text(newText);

   });
}

function submitGeofences(){
console.log("sending geofences to server");
    var geofences = [];
    drawnItems.eachLayer(function (layer){
    console.log(layer);
    var geofence = new Object();
    geofence.latlngs = layer.getLatLngs()[0];
    for(var k in layer.feature.properties) geofence[k]=layer.feature.properties[k];
    geofence.properties = layer.feature.properties;
    console.log(geofence);
    geofences.push(geofence);
    })
console.log(geofences);
    getLoadCount(geofences);
}

function getLoadCount(data){
$.ajax({
       type: "POST",
       url: "/geofence",
       data: JSON.stringify(data),
       contentType: "application/json; charset=utf-8",
       dataType: "json",


   }).then(function (data){
    console.log(data);
    var oldText =  "Number of Loads completed: ";
    var newText = oldText + data.content;
     $( "#result" ).text(newText);

   });
}

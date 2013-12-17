<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<title>Demography</title>

<meta name="viewport" content="width=device-width, initial-scale=1.0">

<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/style.css">
<style type="text/css">
.labels {
    color: red;
    background-color: white;
    font-family: "Lucida Grande", "Arial", sans-serif;
    font-size: 10px;
    font-weight: bold;
    text-align: center;
    width: 40px;
    border: 2px solid black;
    white-space: nowrap;
}
</style>

</head>

<body>
    <div class="navbar navbar-inverse navbar-static-top">


        <div class="container">
            <a href="#" class="navbar-brand">Demography Search</a>


            <!-- <button class = "navbar-toggle" data-toggle = "collapse" data-target=".navHeaderCollapse">
          <span class = "icon-bar"></span>
          <span class = "icon-bar"></span>
          <span class = "icon-bar"></span>


        </button>
        <div class="collapse navbar-collapse navHeaderCollapse">


          <ul class = "nav navbar-nav navbar-right">
            <li class = "active"><a href="#">Home</a></li>
            <li><a href="#">Categories</a></li>
            <li><a href="#">About Us</a></li>
            <li><a href="#">Contact</a></li>
          </ul>

        </div>  -->

        </div>
    </div>


    <div class="everything">
        <div class="container" style="text-align: center" id="searchForm">
            <form class="navbar-form " role="search" name="demography" action=""
                method="POST">
                <div class="form-group">
                    <div style="padding-bottom: 10px">
                        <label class="label label-info" for="keyword">Doctor</label> <input
                            type="text" class="form-control" placeholder="Doctor" id="query"
                            value="">
                    </div>
                    <div style="padding-bottom:10px">
                        <label class="label label-info" for="category">Category</label> 
                    
                    <select class="form-control" id="category">
                    <option>All</option>
					  <option>Ophthalmologist</option>
						<option>Optometrist</option>
						<option>Allergist</option>
						<option>Anesthesiologist</option>
						<option>Cardiologist</option>
						<option>CriticalCare</option>
						<option>Dermatologist</option>
						<option>EarNoseThroat </option>
						<option>EmergencyMedicine </option>
						<option>Endocrinologist</option>
						<option>Gastroenterologist</option>
						<option>Geneticist</option>
						<option>GeriatricMedicine </option>
						<option>HospiceCarePalliative </option>
						<option>InfectiousDisease </option>
						<option>Nephrologist</option>
						<option>Neurologist</option>
						<option>NeuromusculoskeletalMedicine </option>
						<option>NuclearMedicine </option>
						<option>Oncologist</option>
						<option>PainManagement</option>
						<option>Pathologist</option>
						<option>Podiatrist</option>
						<option>PreventiveMedicine </option>
						<option>Pulmonologist</option>
						<option>Radiologist</option>
						<option>SleepMedicine </option>
						<option>Urologist</option>
						<option>CardiothoracicSurgeon</option>
						<option>ColorectalSurgeon</option>
						<option>GeneralSurgeon</option>
						<option>Neurosurgeon</option>
						<option>OralSurgeon</option>
						<option>Psychiatrist</option>
					  
					</select>
                    
					
					</div>
                    <div style="padding-bottom: 10px;display: none;">
                        <label class="label label-info" for="miles">Miles</label> <input
                            type="text" class="form-control" placeholder="Miles" id="miles"
                            value="3">
                    </div>
                    <div style="padding-bottom: 10px">
                        <label class="label label-info" for="currentLocation">Zip
                            Code</label> <input type="text" class="form-control"
                            placeholder="Zip Code" id="zip">
                    </div>
                    <input type="submit" class="btn btn-primary" value="Search">
                </div>
        </div>
        </form>
        <div id="loading" style="text-align:center"></div>
    </div>

    <div class="container">
        <button type='button' class="btn btn-info" id="newSearch">New
            Search</button>
        <button type='button' class="btn btn-info" id="back"
            onclick="location.href='#'">Back to Results</button>
        <div id="map-canvas"></div>
        <div id="direction">
            <ul class="nav nav-tabs" id="direction-tabs">
                <li class="" value="DRIVING"><a href="#" data-mode="DRIVING">Driving</a></li>
                <li class="" value="TRANSIT"><a href="#" data-mode="TRANSIT">Transit</a></li>
                <li class="" value="BICYCLING"><a href="#"
                    data-mode="BICYCLING">Bicycling</a></li>
                <li class="active" value="WALKING"><a href="#"
                    data-mode="WALKING">Walking</a></li>
            </ul>
            <div id="direction-steps"></div>
        </div>

        <table class="table table-striped" id="tableID">
        </table>
    </div>

<div class="container" id="doctorResult"></div>
    </div>
    <div class="container" id="searchResults"></div>
    </div>

    <br>
    <br>
    <br>
    <br>
    <!--End of everything class -->

    <!--</div>
      <div class = "navbar navbar-default navbar-fixed-bottom">
        <div class = "container">
        <p class = "navbar-text pull-left">Site by OneHaystack</p>
        <a href = "http://youtube.com" class = "navbar-btn btn-danger btn pull-right">Subscribe on YouTube</a>
        </div>
      </div>
    -->
    <script
        src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=true"></script>
    <script
        src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>

    <script>

      var directionsDisplay;
      var directionsService = new google.maps.DirectionsService();
      var previousMapResults = null;
      var previousMarker = null;
      var directionDestination = null;
      var pIndex = null;
      var pMode = "WALKING";
      var gLat = null;
      var gLng = null;
      var PORT = 8089;
      $(document).ready(function() {
        $('#newSearch').hide();
        $('#back').hide();
        $('#direction').hide();
        $('#map-canvas').hide();
       /* navigator.geolocation.getCurrentPosition(function(position){
          gLat = position.coords.latitude;
          gLng = position.coords.longitude;
          alert(gLat+" "+gLng);

        },{
              enableHighAccuracy: true
              ,timeout : 5000
          });*/

        navigator.geolocation.getCurrentPosition(
                    function(position) {
                        gLat = position.coords.latitude;
                        gLng = position.coords.longitude;
                    },
                    function(error){
                        alert(error.message);
                    }, {
                        enableHighAccuracy: true
                        ,timeout : 15000
                    }
            );
      });

      function getCoOrdinates(position){
        gLat = position.coords.latitude;
        gLng = position.coords.longitude;
        alert(gLat+" "+gLng);
      }


       //getting data id of element clicked
       // drawing map


       $(function() {
    	  $('#doctorResult').on('click','a', function(){
    		 $('#doctorResult').hide();
    		 $('#newSearch').hide();
             $('#back').show();
             var results = servletdata.results;
             var val = $(this).attr('data-id');
             i = val;
             var div = $('<div class="list-group">');
             var str = "<a href='#' class='list-group-item' data-id='" + val + "' id='item'>";
             var a = $(str).appendTo(div);
             var list = $('<h4 class="list-group-item-heading">').html(
                      + ': ' + results[i].name).appendTo(a);
             //var ul = $('<ul class = "list-group">').appendTo(list);
             //var span = $('<span class="details">').appendTo(ul);
             var li = $('<p class = "list-group-item-text">').html(
                     results[i].address + '</br>' + results[i].phone
                             
                             + '</br> Category: ' + results[i].category + '</br>'
                             + '<i>'
                             + results[i].distance + ' miles </i>')
                     .appendTo(list);
             //$(result).appendTo(div);
             $('#searchResults').show();
             $('#searchResults').html(div);
             
    	  }); 
       });
      $(function() {
        $('#searchResults').on('click', 'a', function() {
          $('#searchResults').hide();
          $('#newSearch').hide();
          $('#back').show();
         $('#map-canvas').show();
          //$('.everything').hide();
          //$('.map').show();
          var val = $(this).attr('data-id');
          pIndex = val;
          console.log('VAL: ' + val);
          var destLat = servletdata.results[val].lat;
          var destLng = servletdata.results[val].lng;
          var srcLat = servletdata.sourceLat;
          var srcLng = servletdata.sourceLng;
          var end = servletdata.results[val].address;
          //alert(end);
          //google.maps.event.addDomListener(window, 'load', initialize);//initialize(srcLat,srcLng,destLat,destLng);
          //calcRoute();
          initialize(srcLat, srcLng, destLat, destLng,end);
          //initialize(srcLat,srcLng,);
        });
      });

      $(function(){
        $('#direction-tabs').on('click','a',function(){
          
          var listItems = $('#direction-tabs li');
          var mode = $(this).attr('data-mode');
          pMode = mode;
          listItems.each(function(idx,li){
            var tempMode = $(li).attr('value');
            $(this).attr('class',' ');
            if(mode==tempMode){
              $(this).attr('class','active');
            }
          var val = pIndex;  
          var destLat = servletdata.results[val].lat;
          var destLng = servletdata.results[val].lng;
          var srcLat = servletdata.sourceLat;
          var srcLng = servletdata.sourceLng;
          var end = servletdata.results[val].address;
          //alert(end);
          //google.maps.event.addDomListener(window, 'load', initialize);//initialize(srcLat,srcLng,destLat,destLng);
          //calcRoute();
          initialize(srcLat, srcLng, destLat, destLng,end);
          });
          
          
        });
      });

       ///////////////////////////////////////////////////////////
       function printTextDirections(r){
          $('#direction').slideDown(1000);
          var legs = r.routes[0].legs[0];
          var distance = legs.distance;
          var duration = legs.duration;
          var startAddress = legs.start_address;
          var endAddress = legs.end_address;
          var steps = legs.steps;
          var div = $('<div class="list-group">');
          console.log(steps);
          for(var stepIndex = 0 ; stepIndex<steps.length; stepIndex++){
                 //tableData+="<tr><td>"+results[i].name+"</td><td>"+results[i].address+"</td><td>"+results[i].distance+"</td></tr>"
                  var str = "<a href='#' class='list-group-item' data-directionStep='" + stepIndex + "' id='item'>";
                  var a = $(str).appendTo(div);
                  var list = $('<h5 class="list-group-item-heading">').html((stepIndex+1) + ': ' + steps[stepIndex].instructions).appendTo(a);
                  //var ul = $('<ul class = "list-group">').appendTo(list);
                  //var span = $('<span class="details">').appendTo(ul);
                  var li = $('<p class = "list-group-item-text">').html(steps[stepIndex].duration.text + '</br>' + '<i>' + steps[stepIndex].distance.text + '</i>').appendTo(list);
                  //$(result).appendTo(div);
          }

                $('#direction-steps').html(div);
      }
       

       //Calculate the route of the shortest distance we found.

      function calculateRoute(slat, slng, dlat, dlng,end) {

        var tMode = pMode;


        var request = {
          origin: new google.maps.LatLng(slat, slng),
          destination: new google.maps.LatLng(dlat, dlng),
          // waypoints: waypoints,
          optimizeWaypoints: true,
          travelMode: google.maps.TravelMode[tMode]
        };
        directionsService.route(request, function(result, status) {
          if (status == google.maps.DirectionsStatus.OK) {
            console.log(result);
            directionsDisplay.setDirections(result);
            printTextDirections(result);
          }
        });
      }

      function initialize(slat, slng, dlat, dlng,end) {

        directionsDisplay = new google.maps.DirectionsRenderer();
        var centerPosition = new google.maps.LatLng(slat, slng);
        var options = {
          zoom: 12,
          center: centerPosition,
          mapTypeId: google.maps.MapTypeId.ROADMAP
        };
        //$('#map').append('init');
        map = new google.maps.Map(document.getElementById('map-canvas'), options);
        //geocoder = new google.maps.Geocoder();
        directionsDisplay.setMap(map);
        calculateRoute(slat, slng, dlat, dlng,end);
        //calculateDistances(slat,slng,dlat,dlng);
      }


       /////////////////////////////////////////////////////
      $(function() {
        $('#newSearch').click(function() {
          $('#map-canvas').hide(1000, function() {          
            
          });
          $('#newSearch').slideUp();
          $('#searchForm').show(1000);
          $('#searchResults').hide(1000);
          $('#doctorResult').hide(1000);

        });

        $('#back').on('click',function(){
          previousMarker.setMap(previousMapResults);
          console.log(previousMapResults);
          console.log(previousMarker);
          drawPoints(servletdata,servletdata.sourceLat,servletdata.sourceLng);
          $('#doctorResult').show(500);
          $(this).hide(function(){
            $('#newSearch').show(500);
            $('#direction').slideUp(500);
          });
          
          //---
          $('#map-canvas').hide();
          
        });
      });

      var realLat = null;
      var realLng = null;
      var miles = null;
      var zip = null;
      var qS = null;
      var url = null;
      var servletdata = null;
      var category = null;
      var letterArray = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'];

      function getPositions(position) {
        realLat = position.coords.latitude;
        realLng = position.coords.longitude;
        //alert("done");
        begin(gLat, gLng);
      }

      function mapWithZip() {
        var url = "<%=request.getContextPath()%>/search/doctorsearch?query=" + encodeURIComponent(qS).replace(/\%20/g, '%3d') + "&miles=" + miles + "&zip=" + encodeURIComponent(zip).replace(/\%20/g, '%3d');
        url += "&lat="+gLat+"&lng="+gLng+"&name=doctors&practice="+category+"";
        //$("#debug").html("didNotGetPositions");
        //alert(url);
        console.log("Final URL2: " + url);
       /* $.post(url, $(this).serialize(),
          function(json) {
            servletdata = json;
            getTable(json);
            //$('#tableID').html(table);
            $('#map-canvas').show(1000, function() {
              
              $('#newSearch').slideDown(1000);
              $('#searchResults').slideDown(1000);
              drawPoints(json, null, null);
            });
            $('#searchForm').hide(1000);                   
            
          }, 'json');*/
        $.ajax({
          type:'get',
          contentType:"application/json",
          dataType:"json",
          url:url,
          data: {  },
          beforeSend: function() {
              // make nice image to show the request is running
              //$('searchResults').html()
              $('#loading')
              .html(
                      '<img src="http://www.smscentral.com.au/wp-content/uploads/loading-gif.gif"/>');
          },
          success: function(json) {
              $('#loading').html('');
              servletdata = json;
            getTable(json);
            //$('#tableID').html(table);
            //---$('#map-canvas').show(1000, function() {
              
              $('#newSearch').slideDown(1000);
              $('#doctorResult').slideDown(1000);
              drawPoints(json, null, null);
            //---});
            
            $('#searchForm').hide(1000);
          },
          error: function(jq,status,message) {
              alert('A jQuery error has occurred. Status: ' + status + ' - Message: ' + message);
          }
        });

      }

      function begin(realLat, realLng) {
        realLat = gLat;
        realLng = gLng;
        zip = null;
        url = "<%=request.getContextPath()%>/search/doctorsearch?query="
                    + encodeURIComponent(qS).replace(/\%20/g, '%3d');
            url += "&miles=" + miles;
            url += "&zip=null";
            //alert("hey");
            url += "&lat=" + realLat;
            url += "&lng=" + realLng;
            url +="&name=doctors";
            url +="&practice="+category;
            //$("#debug").html("gotPositions");
            //alert(url);
            console.log("Final URL: " + url);
            /*$.post(url, $(this).serialize(),
              function(json) {
                alert("hey");
                servletdata = json;
                getTable(json);
                //alert(table);
                //$('#tableID').html(table);  
                //getGeoLocation();  
                $('#map-canvas').show(1000, function() {
                  
                  $('#newSearch').slideDown(1000);
                  $('#searchResults').slideDown(1000);
                  drawPoints(json, realLat, realLng);
                });
                $('#searchForm').hide(1000);
                alert("heyllo");                            
              }, 'json');*/

            $
                    .ajax({
                        type : 'get',
                        contentType : "application/json",
                        dataType : "json",
                        url : url,
                        data : {},
                        beforeSend : function() {
                            // make nice image to show the request is running
                            $('#loading')
                                    .html(
                                            '<img src="http://www.smscentral.com.au/wp-content/uploads/loading-gif.gif"/>');
                        },
                        success : function(json) {
                            $('#loading').html('');
                            servletdata = json;
                            getTable(json);
                            //alert(table);
                            //$('#tableID').html(table);  
                            //getGeoLocation();  
                            //---$('#map-canvas').show(1000, function() {

                                $('#newSearch').slideDown(1000);
                                $('#doctorResult').slideDown(1000);
                                drawPoints(json, realLat, realLng);
                            //---});
                            $('#searchForm').hide(1000);
                        },
                        error : function(jq, status, message) {
                            alert('A jQuery error has occurred. Status: '
                                    + status + ' - Message: ' + message);
                        }
                    });
        }

        $(function() {
            $('form[name=demography]').submit(
                    function() {
                        miles = $('#miles').val();
                        zip = $('#zip').val();
                        qS = $('#query').val();
                        qS = qS.replace(/^\s+|\s+$/g, '');
                        zip = zip.replace(/^\s+|\s+$/g, '');
                        category = $('#category').val();
                        //alert(category);
                        //console.log(navigator.geolocation);
                        //alert(zip);
                        if(qS==''){
                        	qS="null";
                        }
                        var flag = null;
                        if (zip == '')
                            flag = navigator.geolocation;
                        //alert(flag);
                        if (flag) {
                            navigator.geolocation.getCurrentPosition(begin,
                                    mapWithZip);
                            //,{timeout:10000});

                            //$("#debug").html("how the heck can it come here");
                        } else {
                            //$("#debug").html("brower mistake");
                            mapWithZip();
                            // alert("Doesn't work on your browser"); 
                        }

                        return false;
                    });
        });

        function getTable(data) {
            var results = data.results;
            var tableData = "";
            //alert(data.results.length);
            var div = $('<div class="list-group">');
            var len = results.length;
            if (results.length > 26) {
                len = 26;
            }
            for (var i = 0; i < len; i++) {
                //tableData+="<tr><td>"+results[i].name+"</td><td>"+results[i].address+"</td><td>"+results[i].distance+"</td></tr>"
                var str = "<a href='#' class='list-group-item' data-id='" + i + "' id='item'>";
                var a = $(str).appendTo(div);
                var list = $('<h4 class="list-group-item-heading">').html(
                        letterArray[i] + ': ' + results[i].name).appendTo(a);
                //var ul = $('<ul class = "list-group">').appendTo(list);
                //var span = $('<span class="details">').appendTo(ul);
                var li = $('<p class = "list-group-item-text">').html(
                        results[i].address + '</br>' + results[i].phone
                                
                                + '</br> Category: ' + results[i].category + '</br>'
                                + '<i>'
                                + results[i].distance + ' miles </i>')
                        .appendTo(list);
                //$(result).appendTo(div);
            }

            $('#doctorResult').html(div);

            //var table = "";
            //table+="<thead><tr><th>Name</th><th>Address</th><th>Distance</th></tr></thead>";
            //table+=tableData;
            return tableData;

            /////////////////////

        }

        function drawPoints(data, realLat, realLng) {
            console.log(data);
            var results = data.results;

            var pinColor = "1569C7";
            /*var pinImage = new google.maps.MarkerImage("http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|" + pinColor,
              new google.maps.Size(21, 34),
              new google.maps.Point(0, 0),
              new google.maps.Point(10, 34));
            var pinShadow = new google.maps.MarkerImage("http://chart.apis.google.com/chart?chst=d_map_pin_shadow",
              new google.maps.Size(40, 37),
              new google.maps.Point(0, 0),
              new google.maps.Point(12, 35));
             */
            var currentLocation = null;
            if (realLat == null) {
                currentLocation = new google.maps.LatLng(data.sourceLat,
                        data.sourceLng);
            } else {
                //alert("wtf");
                currentLocation = new google.maps.LatLng(realLat, realLng);
            }
            
            var averageDistance = 5;
            var sum = 0;
            for(var g = 0; g<results.length; g++){
            	sum = sum+results[g].distance;
            }
            averageDistance = sum/results.length+1;
            var zoom = 12;
            
            if(averageDistance>2 && averageDistance <10){
            	zoom = 9;
            }else if(averageDistance>=10 && averageDistance <200){
            	zoom = 8;
            }else if(averageDistance>=200 ){
            	zoom = 2;
            	
            }
            
            var mapOptions = {
                zoom : zoom,
                center : currentLocation,
                mapTypeId : google.maps.MapTypeId.ROADMAP
            }
            //alert('Hey');
            var map = new google.maps.Map(
                    document.getElementById('map-canvas'), mapOptions);

            // alert(data.sourceLat);

            var letter = '';

            var len = results.length;
            if (results.length > 26) {
                len = 26;
            }
            var marker = null;

            for (var i = 0; i < len; i++) {
                var lat = results[i].lat;
                var lng = results[i].lng;
                letter = letterArray[i];
                //alert(letter);
                //var referenceUrl = "http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=" + letter + "|FE6256|000000";
                var referenceUrl = "mapMarkers/red_Marker" + letter + ".png";//.toLowerCase()+".png"
                if (lat != 0 && lng != 0) {
                    marker = new google.maps.Marker({
                        position : new google.maps.LatLng(lat, lng),
                        map : map,
                        //labelContent:i,
                        //labelContent: "A",
                        //draggable: true,
                        //raiseOnDrag: true,
                        //labelContent: results[i].name,
                        animation : google.maps.Animation.DROP,
                        // labelAnchor: new google.maps.Point(3,30),
                        //labelClass: "labels", // the CSS class for the label
                        //labelInBackground: false,
                        //labelStyle: {opacity: 0.6},
                        icon : referenceUrl
                    });
                    google.maps.event.addListener(marker, "click", function() {

                        if (marker.getAnimation() != null) {
                            marker.setAnimation(null);
                        } else {
                            marker.setAnimation(google.maps.Animation.BOUNCE);
                        }
                    });

                }

            }

            //alert("Done");
            //set marker for Current location on the same 'map'
            var marker = new google.maps.Marker(
                    {
                        position : currentLocation,
                        map : map,
                        icon : "http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=.|1e477a|000000",
                        //shadow: pinShadow,
                        animation : google.maps.Animation.BOUNCE,
                    //title:'You'

                    });

            var mapWidth = $(window).width();
            var mapHeight = $(window).height();
            mapWidth = 0.9 * mapWidth;
            if (mapWidth > 400)
                mapWidth = 400;
            $('#map-canvas').css('height', mapHeight * 0.25);
            $('#map-canvas').css('width', mapWidth);
            // alert(mapWidth);
            previousMapResults = map;
            previousMarker = marker;
            marker.setMap(map);

        }
    </script>
</body>
</html>
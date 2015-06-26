define(function(require) {

  'use strict';

  var React = require('react');

  return React.createClass({

    getDefaultProps: function () {
      return {
        mapOptions: {
          center: new google.maps.LatLng(33.1955102, 136.9374724),
          zoom: 5
        },
        markers: []
      };
    },

    componentDidMount: function () {
      this.setState({
        map: new google.maps.Map(this.getDOMNode(), this.props.mapOptions)
      });
    },

    makeMarkers: function(locations) {
      var latlng = [];
      for(var i=0; i < locations.length; i++) {
        latlng.push(
          new google.maps.LatLng(
            locations[i].latitude,
            locations[i].longitude
          ));
      }
      for(var i=0; i <latlng.length; i++) {
        this.props.markers.push(
          new google.maps.Marker({
            position: latlng[i],
            map: this.state.map,
            title: 'Gojira!'
          }));
      }
    },

    clearMarkers: function() {
      for(var i=0; i < this.props.markers.length; i++) {
        this.props.markers[i].setMap(null);
      }
    },

    updateLocations: function() {
      this.clearMarkers();
      this.makeMarkers(this.props.locations);
    },

    componentDidUpdate: function() {
      this.updateLocations();
    },

    render: function () {
      return (
          <div className='map-godzilla'>
          </div>
      );
    }

  });

});



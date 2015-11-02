define(function(require) {

  'use strict';

  var React = require('react');
  var Toolbar = require('jsx!godzilla/toolbar');
  var Map = require('jsx!godzilla/map');

  var App = React.createClass({

    getDefaultProps: function() {
      return {
        deviation: 16
      };
    },

    getInitialState: function() {
      return {
        locations: []
      };
    },

    getLocations: function() {
      $.ajax({
        'type': 'GET',
        'url': '/locations/',
        'contentType': 'application/json',
        'async': 'false',
        'success' : function(data) {
          if(this.isMounted()) {
            this.setState({
              locations: data
            });
          }
        }.bind(this),
        'error': function(data) {
          console.log("error");
        }.bind(this)
      });
    },

    componentDidMount: function() {
      this.getLocations();
    },

    filter: function(deviation) {
      this.props.deviation = deviation;
      this.getLocations();
    },

    render: function () {
      return (
        <div>
          <Toolbar filter={this.filter}/>
          <Map
           locations={this.state.locations}
           deviation={this.props.deviation}/>
        </div>
      );
    }

  });

  App.init = function () {
      React.render(<App/>, document.getElementById('godzilla'));
  };

  return App;

});

define(function(require) {

  'use strict';

  var React = require('react');
  var ReactBootstrap = require('react-bootstrap');
  var ButtonToolbar = ReactBootstrap.ButtonToolbar;
  var Button = ReactBootstrap.Button;

  return React.createClass({

    filterLowClick: function() {
      this.props.filter(13);
    },
    filterMediumClick: function() {
      this.props.filter(16);
    },
    filterHighClick: function() {
      this.props.filter(18);
    },

    render: function() {
      return (
        <div id="panel">
          <ButtonToolbar>
            <Button bsSize='small' disabled>Refinement</Button>
            <Button
              bsStyle='info'
              bsSize='small'
              onClick={this.filterLowClick}>Low</Button>

            <Button
              bsStyle='warning'
              bsSize='small'
              onClick={this.filterMediumClick}>Med</Button>

            <Button
              bsStyle='danger'
              bsSize='small'
              onClick={this.filterHighClick}>High</Button>
          </ButtonToolbar>
        </div>
      );
    }
  });

});

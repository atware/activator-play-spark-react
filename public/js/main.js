// `main.js` is the file that sbt-web will use as an entry point
(function (requirejs) {
  'use strict';

  // -- RequireJS config --
  requirejs.config({
    // Packages = top-level folders; loads a contained file named 'main.js"
    packages: ['godzilla'],
    paths: {
      'react': ['../lib/jsx-requirejs-plugin/js/react-with-addons'],
      'JSXTransformer': ['../lib/jsx-requirejs-plugin/js/JSXTransformer'],
      'jsx': ['../lib/jsx-requirejs-plugin/js/jsx'],
      'jquery': ['../lib/jquery/jquery'],
      'react-router' : ['../lib/react-router/0.13.2/ReactRouter'],
      'react-router-shim': 'react-router-shim',
      'text': ['../lib/requirejs-text/text'],
      'bootstrap': ['../lib/bootstrap/js/bootstrap'],
      'react-bootstrap': ['../lib/react-bootstrap/react-bootstrap']
    },
    jsx: {
      fileExtension: '.jsx'
    },
    shim : {
      'react': {
        deps: ['jquery'],
        exports: 'react'
      },
      'react-router': {
        deps:    ['react'],
        exports: 'Router'
      },
      'bootstrap': ['jquery'],
      'react-bootstrap': {
        deps: ['react', 'bootstrap'],
        exports: 'ReactBootstrap'
      }
    }
  });

  requirejs.onError = function (err) {
    console.log(err);
  };

  require(['jsx!app'], function(App){
    console.log('initializing app');
    App.init();

  });

})(requirejs);

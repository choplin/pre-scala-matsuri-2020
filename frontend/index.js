import {
  Elm
} from './elm/Main.elm'

import * as scalajs from '../scala/js/target/scala-2.13/shared-fastopt'

var app = Elm.Main.init({
  node: document.getElementById('myapp')
});

app.ports.createTodo.subscribe(function (title) {

})
window.app = app;
window.scalajs = scalajs;

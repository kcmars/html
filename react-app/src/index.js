import React from 'react';
import ReactDOM from 'react-dom';
import './style/index.css';
import App from './router/App';
import registerServiceWorker from './other/registerServiceWorker';

ReactDOM.render(<App />, document.getElementById('root'));
registerServiceWorker();

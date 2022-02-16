import './App.css';
import Grid from './components/Grid/Grid';
import Header from './components/Header';
import Keyboard from './components/Keyboard/Keyboard';

function App() {
  return (
    <div className="App">
      <div className="App-body">
        <Header />
        <Grid />
        <Keyboard />
      </div>
    </div>
  );
}

export default App;

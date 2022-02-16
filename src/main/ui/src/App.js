import './App.css';
import Grid from './components/Grid/Grid';
import Header from './components/Header';
import Keyboard from './components/Keyboard/Keyboard';
import Recommendation from './components/Recommendation/Recommendation';

function App() {
  return (
    <div className="App">
      <div className="App-body">
        <Header />
        <Recommendation />
        <Grid />
        <Keyboard />
      </div>
    </div>
  );
}

export default App;

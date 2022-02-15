import './App.css';
import Grid from './components/Grid';
import TopBar from './components/TopBar';

function App() {
  return (
    <div className="App">
      <div className="App-body">
        <TopBar />
        <Grid />
        Keyboard
      </div>
    </div>
  );
}

export default App;

import { BrowserRouter as Router } from "react-router-dom";
import { AuthProvider } from "./store/authStore";
import AppRoutes from "./routes";

function App() {
  return (
    <AuthProvider>
      <Router>
        <AppRoutes />
      </Router>
    </AuthProvider>
  );
}

export default App;

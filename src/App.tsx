import { BrowserRouter as Router } from "react-router-dom";
import { AuthProvider } from "./features/auth/services/authContext";
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

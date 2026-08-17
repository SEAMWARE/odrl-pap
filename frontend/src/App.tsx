/**
 * Root application component.
 *
 * Wraps the router tree with I18nProvider and ThemeProvider so that
 * localized strings and theme CSS custom properties are available
 * to all descendant components.
 */
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { I18nProvider } from './i18n';
import { ThemeProvider } from './theme/ThemeContext';
import Layout from './components/Layout';
import PolicyList from './pages/PolicyList';
import PolicyEditor from './pages/PolicyEditor';
import TemplateList from './pages/TemplateList';
import TemplateEditor from './pages/TemplateEditor';

/**
 * Application root with i18n and theme context providers.
 */
function App() {
  return (
    <I18nProvider>
      <ThemeProvider>
        <Router>
          <Routes>
            <Route path="/" element={<Layout />}>
              <Route index element={<PolicyList />} />
              <Route path="new" element={<PolicyEditor />} />
              <Route path="edit/:id" element={<PolicyEditor />} />
              <Route path="templates" element={<TemplateList />} />
              <Route path="templates/new" element={<TemplateEditor />} />
              <Route path="templates/edit/:id" element={<TemplateEditor />} />
            </Route>
          </Routes>
        </Router>
      </ThemeProvider>
    </I18nProvider>
  );
}

export default App;

/**
 * Application layout component.
 *
 * Renders the shared navigation bar and wraps page content via
 * React Router's `<Outlet>`. Provides links to Policies and Templates.
 */
import { Outlet } from 'react-router-dom';
import { Navbar, Container, Nav } from 'react-bootstrap';

/**
 * Top-level layout with navigation bar and content outlet.
 */
const Layout = () => {
  return (
    <>
      <Navbar bg="dark" variant="dark" expand="lg">
        <Container>
          <Navbar.Brand href="/">ODRL PAP</Navbar.Brand>
          <Navbar.Toggle aria-controls="basic-navbar-nav" />
          <Navbar.Collapse id="basic-navbar-nav">
            <Nav className="me-auto">
              <Nav.Link href="/">Policies</Nav.Link>
              <Nav.Link href="/templates">Templates</Nav.Link>
            </Nav>
          </Navbar.Collapse>
        </Container>
      </Navbar>
      <Container className="mt-4">
        <Outlet />
      </Container>
    </>
  );
};

export default Layout;

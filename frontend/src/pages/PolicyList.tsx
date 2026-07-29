/**
 * Policy list page component.
 *
 * Displays all stored ODRL policies in a table with edit and delete
 * actions. Provides a link to create new policies.
 */
import { useEffect, useState } from 'react';
import { Table, Button } from 'react-bootstrap';
import { PapService } from '../api/services/PapService';
import type { Policy } from '../services/api';
import { Link } from 'react-router-dom';

/**
 * Renders a table of all ODRL policies with CRUD actions.
 */
const PolicyList = () => {
  const [policies, setPolicies] = useState<Policy[]>([]);

  useEffect(() => {
    PapService.getPolicies()
      .then(setPolicies)
      .catch(console.error);
  }, []);

  /** Deletes a policy by ID and removes it from the displayed list. */
  const handleDelete = (id: string) => {
    PapService.deletePolicyById(id)
      .then(() => {
        setPolicies(policies.filter(p => p.id !== id));
      })
      .catch(console.error);
  };

  return (
    <>
      <h1>Policies</h1>
      <Link to="/new" className="btn btn-primary mb-3" aria-label="Create a new policy">New Policy</Link>
      <Table striped bordered hover aria-label="Policy list">
        <thead>
          <tr>
            <th scope="col">ID</th>
            <th scope="col">ODRL UID</th>
            <th scope="col">Actions</th>
          </tr>
        </thead>
        <tbody>
          {policies.map(policy => (
            <tr key={policy.id}>
              <td>{policy.id}</td>
              <td>{policy['odrl:uid']}</td>
              <td>
                <Link
                  to={`/edit/${policy.id}`}
                  className="btn btn-sm btn-primary me-2"
                  aria-label={`Edit policy ${policy.id}`}
                >
                  Edit
                </Link>
                <Button
                  variant="danger"
                  size="sm"
                  onClick={() => handleDelete(policy.id!)}
                  aria-label={`Delete policy ${policy.id}`}
                >
                  Delete
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
    </>
  );
};

export default PolicyList;
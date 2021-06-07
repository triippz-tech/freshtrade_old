import React from 'react';
import { DropdownItem } from 'reactstrap';
import { NavDropdown } from './menu-components';
import { Translate, translate } from 'react-jhipster';
import { MDBLink } from 'mdbreact';

const adminMenuItems = (
  <>
    <DropdownItem>
      <MDBLink to="/admin/user-management">
        <Translate contentKey="global.menu.admin.userManagement">User management</Translate>
      </MDBLink>
    </DropdownItem>
    <DropdownItem>
      <MDBLink to="/admin/tracker">
        <Translate contentKey="global.menu.admin.tracker">User tracker</Translate>
      </MDBLink>
    </DropdownItem>
    <DropdownItem>
      <MDBLink to="/admin/metrics">
        <Translate contentKey="global.menu.admin.metrics">Metrics</Translate>
      </MDBLink>
    </DropdownItem>
    <DropdownItem>
      <MDBLink to="/admin/health">
        <Translate contentKey="global.menu.admin.health">Health</Translate>
      </MDBLink>
    </DropdownItem>
    <DropdownItem>
      <MDBLink to="/admin/configuration">
        <Translate contentKey="global.menu.admin.configuration">Configuration</Translate>
      </MDBLink>
    </DropdownItem>
    <DropdownItem>
      <MDBLink to="/admin/logs">
        <Translate contentKey="global.menu.admin.logs">Logs</Translate>
      </MDBLink>
    </DropdownItem>
  </>
);

const openAPIItem = (
  <DropdownItem>
    <MDBLink to="/admin/docs">
      <Translate contentKey="global.menu.admin.apidocs">API</Translate>
    </MDBLink>
  </DropdownItem>
);

export const AdminMenu = ({ showOpenAPI }) => (
  <NavDropdown icon="fas fa-cogs" name={translate('global.menu.admin.main')} id="admin-menu" data-cy="adminMenu">
    {adminMenuItems}
    {showOpenAPI && openAPIItem}
  </NavDropdown>
);

export default AdminMenu;

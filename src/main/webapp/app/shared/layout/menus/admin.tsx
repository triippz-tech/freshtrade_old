import React from 'react';
import { DropdownItem } from 'reactstrap';
import { NavDropdown } from './menu-components';
import { Translate, translate } from 'react-jhipster';
import { Link } from 'react-router-dom';

const adminMenuItems = (
  <>
    <Link to="/admin/user-management">
      <DropdownItem>
        <Translate contentKey="global.menu.admin.userManagement">User management</Translate>
      </DropdownItem>
    </Link>
    <Link to="/admin/tracker">
      <DropdownItem>
        <Translate contentKey="global.menu.admin.tracker">User tracker</Translate>
      </DropdownItem>
    </Link>
    <Link to="/admin/metrics">
      <DropdownItem>
        <Translate contentKey="global.menu.admin.metrics">Metrics</Translate>
      </DropdownItem>
    </Link>
    <Link to="/admin/health">
      <DropdownItem>
        <Translate contentKey="global.menu.admin.health">Health</Translate>
      </DropdownItem>
    </Link>
    <Link to="/admin/configuration">
      <DropdownItem>
        <Translate contentKey="global.menu.admin.configuration">Configuration</Translate>
      </DropdownItem>
    </Link>
    <Link to="/admin/logs">
      <DropdownItem>
        <Translate contentKey="global.menu.admin.logs">Logs</Translate>
      </DropdownItem>
    </Link>
  </>
);

const openAPIItem = (
  <Link to="/admin/docs">
    <DropdownItem>
      <Translate contentKey="global.menu.admin.apidocs">API</Translate>
    </DropdownItem>
  </Link>
);

export const AdminMenu = ({ showOpenAPI }) => (
  <NavDropdown icon="fas fa-cogs" name={translate('global.menu.admin.main')} id="admin-menu">
    {adminMenuItems}
    {showOpenAPI && openAPIItem}
  </NavDropdown>
);

export default AdminMenu;

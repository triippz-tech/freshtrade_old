import React from 'react';
import { Translate, translate } from 'react-jhipster';
import { NavDropdown, NavDropdownUser } from './menu-components';
import { DropdownItem, MDBLink } from 'mdbreact';

const accountMenuItemsAuthenticated = (
  <>
    <DropdownItem data-cy="settings">
      <MDBLink to="/account/settings">
        <Translate contentKey="global.menu.account.settings">Settings</Translate>
      </MDBLink>
    </DropdownItem>
    <DropdownItem data-cy="passwordItem">
      <MDBLink to="/account/password">
        <Translate contentKey="global.menu.account.password">Password</Translate>
      </MDBLink>
    </DropdownItem>
    <DropdownItem data-cy="logout">
      <MDBLink to="/logout">
        <Translate contentKey="global.menu.account.logout">Sign out</Translate>
      </MDBLink>
    </DropdownItem>
  </>
);

export const accountMenuItems = (
  <>
    <DropdownItem id="login-item" data-cy="login">
      <MDBLink to="/login">
        <Translate contentKey="global.menu.account.login">Sign in</Translate>
      </MDBLink>
    </DropdownItem>
    <DropdownItem data-cy="register">
      <MDBLink to="/account/register">
        <Translate contentKey="global.menu.account.register">Register</Translate>
      </MDBLink>
    </DropdownItem>
  </>
);

export const AccountMenu = ({ isAuthenticated = false, image = undefined }) => (
  <NavDropdownUser name={translate('global.menu.account.main')} id="account-menu" data-cy="accountMenu" image={image ? image : undefined}>
    {isAuthenticated ? accountMenuItemsAuthenticated : accountMenuItems}
  </NavDropdownUser>
);

export default AccountMenu;

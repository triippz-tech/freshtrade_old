import React from 'react';
import { Translate, translate } from 'react-jhipster';
import { NavDropdown } from './menu-components';
import { DropdownItem, MDBLink } from 'mdbreact';

export const EntitiesMenu = props => (
  <NavDropdown
    icon="fas fa-th-list"
    name={translate('global.menu.entities.main')}
    id="entity-menu"
    data-cy="entity"
    style={{ maxHeight: '80vh', overflow: 'auto' }}
  >
    <DropdownItem>
      <MDBLink to="/category">
        <Translate contentKey="global.menu.entities.category" />
      </MDBLink>
    </DropdownItem>
    <DropdownItem>
      <MDBLink to="/image">
        <Translate contentKey="global.menu.entities.image" />
      </MDBLink>
    </DropdownItem>
    <DropdownItem>
      <MDBLink to="/item">
        <Translate contentKey="global.menu.entities.item" />
      </MDBLink>
    </DropdownItem>
    <DropdownItem>
      <MDBLink to="/item-token">
        <Translate contentKey="global.menu.entities.itemToken" />
      </MDBLink>
    </DropdownItem>
    <DropdownItem>
      <MDBLink to="/reservation">
        <Translate contentKey="global.menu.entities.reservation" />
      </MDBLink>
    </DropdownItem>
    <DropdownItem>
      <MDBLink to="/country">
        <Translate contentKey="global.menu.entities.country" />
      </MDBLink>
    </DropdownItem>
    <DropdownItem>
      <MDBLink to="/location">
        <Translate contentKey="global.menu.entities.location" />
      </MDBLink>
    </DropdownItem>
    <DropdownItem>
      <MDBLink to="/trade-event">
        <Translate contentKey="global.menu.entities.tradeEvent" />
      </MDBLink>
    </DropdownItem>
    <DropdownItem>
      <MDBLink to="/user-profile">
        <Translate contentKey="global.menu.entities.userProfile" />
      </MDBLink>
    </DropdownItem>
  </NavDropdown>
);

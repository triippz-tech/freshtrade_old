import React from 'react';
import { Translate, translate } from 'react-jhipster';
import { NavDropdown } from './menu-components';
import { DropdownItem } from 'mdbreact';
import { Link } from 'react-router-dom';

export const EntitiesMenu = props => (
  <NavDropdown
    icon="fas fa-th-list"
    name={translate('global.menu.entities.main')}
    id="entity-menu"
    style={{ maxHeight: '80vh', overflow: 'auto' }}
  >
    <Link to="/category">
      <DropdownItem>
        <Translate contentKey="global.menu.entities.category" />
      </DropdownItem>
    </Link>
    <Link to="/image">
      <DropdownItem>
        <Translate contentKey="global.menu.entities.image" />
      </DropdownItem>
    </Link>
    <Link to="/item">
      <DropdownItem>
        <Translate contentKey="global.menu.entities.item" />
      </DropdownItem>
    </Link>
    <Link to="/item-token">
      <DropdownItem>
        <Translate contentKey="global.menu.entities.itemToken" />
      </DropdownItem>
    </Link>
    <Link to="/reservation">
      <DropdownItem>
        <Translate contentKey="global.menu.entities.reservation" />
      </DropdownItem>
    </Link>
    <Link to="/country">
      <DropdownItem>
        <Translate contentKey="global.menu.entities.country" />
      </DropdownItem>
    </Link>
    <Link to="/location">
      <DropdownItem>
        <Translate contentKey="global.menu.entities.location" />
      </DropdownItem>
    </Link>
    <Link to="/trade-event">
      <DropdownItem>
        <Translate contentKey="global.menu.entities.tradeEvent" />
      </DropdownItem>
    </Link>
    <Link to="/user-profile">
      <DropdownItem>
        <Translate contentKey="global.menu.entities.userProfile" />
      </DropdownItem>
    </Link>
  </NavDropdown>
);

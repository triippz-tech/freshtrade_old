import React from 'react';
import { MDBNavbarBrand } from 'mdbreact';

import AccountMenu from '../menus/account';
import { AdminMenu } from '../menus/admin';
import { EntitiesMenu, LocaleMenu } from 'app/shared/layout/menus';
import { Button, Col, Container, Row } from 'reactstrap';

export interface JumbotronProps {
  locationName: string;
  locationDistance: string;
  onLocationClick: () => void;
  isAuthenticated: boolean;
  isAdmin: boolean;
  isOpenAPIEnabled: boolean;
  currentLocale: string;
  handleLocaleChange: (event) => void;
}

export const Jumbotron: React.FC<JumbotronProps> = props => {
  return (
    <div className="p-3 text-center bg-white border-bottom">
      <Container fluid={true}>
        <Row>
          <Col md="3" className="d-flex justify-content-center justify-content-md-start mb-3 mb-md-0">
            <MDBNavbarBrand href="/" className="ms-md-2">
              <img src="content/images/freshtrade500x100.png" alt="LOGO" height="35" />
            </MDBNavbarBrand>
          </Col>

          <Col md="3">
            <form className="d-flex input-group w-auto my-auto mb-3 mb-md-0">
              <input autoComplete="off" type="search" className="form-control rounded" placeholder="Search for Goods" />
              <span className="input-group-text border-0 d-none d-lg-flex" style={{ backgroundColor: '#24A344' }}>
                <i className="fas fa-search"></i>
              </span>
            </form>
          </Col>

          <Col md="3" className="d-flex justify-content-center justify-content-md-center align-items-center text-sm">
            <Button
              size="sm"
              color="link"
              className="rounded-pill badge-notification location-button"
              onClick={() => props.onLocationClick()}
            >
              <span>
                <i className="fas fa-map-marker text-ft-primary"></i>
              </span>
              {props.locationName}: {props.locationDistance}
            </Button>
          </Col>

          <Col md="3" className="d-flex justify-content-center justify-content-md-end align-items-center">
            <LocaleMenu currentLocale={props.currentLocale} onClick={props.handleLocaleChange} />
            <AccountMenu isAuthenticated={props.isAuthenticated} />
            {props.isAuthenticated && props.isAdmin && <EntitiesMenu />}
            {props.isAuthenticated && props.isAdmin && <AdminMenu showOpenAPI={props.isOpenAPIEnabled} />}
          </Col>
        </Row>
      </Container>
    </div>
  );
};

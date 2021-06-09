import './footer.scss';

import React from 'react';
import { MDBCol, MDBContainer, MDBFooter, MDBRow } from 'mdbreact';
import { Link } from 'react-router-dom';

const Footer = props => (
  <MDBFooter className="text-center text-lg-left ft-footer">
    <MDBContainer>
      <MDBRow>
        <MDBCol md="6">
          &copy; {new Date().getFullYear()} Copyright:{' '}
          <a className="text-dark" href="https://freshtrade.io/">
            FreshTrade
          </a>
        </MDBCol>
        <MDBCol md="6">
          <div className="copyright-menu text-dark">
            <ul>
              <li>
                <Link to="/" className="text-dark">
                  Home
                </Link>
              </li>
              <li>
                <Link to="/terms" className="text-dark">
                  Terms
                </Link>
              </li>
              <li>
                <Link to="/privacy-policy" className="text-dark">
                  Privacy Policy
                </Link>
              </li>
            </ul>
          </div>
        </MDBCol>
      </MDBRow>
    </MDBContainer>
  </MDBFooter>
);

export default Footer;

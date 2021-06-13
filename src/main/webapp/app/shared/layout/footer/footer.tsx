import './footer.scss';

import React from 'react';
import { Link } from 'react-router-dom';
import { Col, Container, Row, Collapse } from 'reactstrap';
import data from 'app/shared/data/footer.json';

const Footer = props => {
  const [columnOpen, setColumnOpen] = React.useState({});

  const toggleColumn = (e, name) => {
    e.preventDefault();
    setColumnOpen({ ...columnOpen, [name]: !columnOpen[name] });
  };
  return (
    <footer>
      <div className="py-4 font-weight-light text-muted">
        <Container>
          <Row className="align-items-center text-sm text-gray-500">
            <Col lg="4" className="text-center text-lg-left">
              <p className="mb-lg-0">&copy; {new Date().getFullYear()} FreshTrade. All rights reserved.</p>
            </Col>
            <Col lg="8">
              <ul className="list-inline mb-0 mt-2 mt-md-0 text-center text-lg-right">
                <li className="list-inline-item">
                  <Link className="text-reset" to="/terms">
                    Terms &amp; Conditions{' '}
                  </Link>
                </li>
                <li className="list-inline-item">
                  <Link className="text-reset" to="/privacy-policy">
                    Privacy{' '}
                  </Link>
                </li>
                <li className="list-inline-item">
                  <Link className="text-reset" to="/about">
                    About{' '}
                  </Link>
                </li>
              </ul>
            </Col>
          </Row>
        </Container>
      </div>
    </footer>
  );
};

export default Footer;

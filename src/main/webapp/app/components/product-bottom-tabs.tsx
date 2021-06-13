import React from 'react';

import { Container, Row, Col, Nav, NavItem, NavLink, TabContent, TabPane } from 'reactstrap';
import { IItem } from 'app/shared/model/item.model';
import CustomMap from 'app/components/maps/custom-map';

interface ProductBottomTabsProps {
  item: IItem;
}

const ProductBottomTabs = (props: ProductBottomTabsProps) => {
  const [activeTab, setActiveTab] = React.useState(1);
  const toggleTab = tab => {
    if (activeTab !== tab) setActiveTab(tab);
  };

  return (
    <section className="mt-5">
      <Container>
        <Nav tabs className="flex-column flex-sm-row">
          <NavItem>
            <NavLink
              className={`detail-nav-link ${activeTab === 1 ? 'active' : ''}`}
              onClick={() => toggleTab(1)}
              style={{ cursor: 'pointer' }}
            >
              Description
            </NavLink>
          </NavItem>
          <NavItem>
            <NavLink
              className={`detail-nav-link ${activeTab === 2 ? 'active' : ''}`}
              onClick={() => toggleTab(2)}
              style={{ cursor: 'pointer' }}
            >
              Event
            </NavLink>
          </NavItem>
        </Nav>
        <TabContent className="py-4" activeTab={activeTab}>
          <TabPane className="px-3" tabId={1}>
            <Row>
              <Col md="7" dangerouslySetInnerHTML={{ __html: props.item.details }} />
            </Row>
          </TabPane>
          <TabPane tabId={2}>
            {props.item.tradeEvent ? (
              <>
                <p>
                  <strong>Event:</strong> {props.item.tradeEvent.eventName}
                </p>
                <p>
                  <strong>Address:</strong> {props.item.tradeEvent.location.address}{' '}
                </p>
                <p>
                  <strong>Start Date:</strong> {props.item.tradeEvent.startDate}
                </p>
                <p>
                  <strong>End Date:</strong> {props.item.tradeEvent.endDate}
                </p>
                <Row>
                  {props.item.tradeEvent ? (
                    <CustomMap
                      key={props.item.tradeEvent.location.id}
                      lat={props.item.tradeEvent.location.latLocation}
                      lng={props.item.tradeEvent.location.longLocation}
                      popupText={props.item.tradeEvent.location.address}
                    />
                  ) : (
                    <CustomMap popupText="Location is not specified" />
                  )}
                </Row>
              </>
            ) : (
              <h5>No Event for this item..</h5>
            )}
          </TabPane>
        </TabContent>
      </Container>
    </section>
  );
};

export default ProductBottomTabs;

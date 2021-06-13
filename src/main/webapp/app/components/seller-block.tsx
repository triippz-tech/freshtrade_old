import React from 'react';
import { Col, Row } from 'reactstrap';
import { IUser } from 'app/shared/model/user.model';

export const SellerBlock = (props: { user: IUser }) => (
  <Row>
    <Col>
      <img className="avatar avatar-64 img-circle" src={props.user.imageUrl ? props.user.imageUrl : 'content/default-user.png'} alt="" />
    </Col>
    <Col className="seller-block-text float-start">
      <p>{props.user.login}</p>
    </Col>
  </Row>
);
export default SellerBlock;

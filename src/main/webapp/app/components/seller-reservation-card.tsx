import React from 'react';
import { Link } from 'react-router-dom';
import { IReservation } from 'app/shared/model/reservation.model';
import { Button, Card, CardBody, CardHeader, Col, Row } from 'reactstrap';
import { APP_DATE_FORMAT_MM_DD_YYYY } from 'app/config/constants';
import { TextFormat } from 'react-jhipster';
import { formatPrice } from 'app/shared/util/transform-utils';

interface SellerReservationCardProps {
  reservation: IReservation;
  onMessageBuyer: (reservation: IReservation) => void;
  onExchangeItem: (reservation: IReservation) => void;
  onCancelReservation: (reservation: IReservation) => void;
}

const getBGColor = (isActive: boolean, isCancelled: boolean) => {};

export const SellerReservationCard: React.FC<SellerReservationCardProps> = props => (
  <Card>
    <CardHeader className="bg-gray">
      <Row>
        <Col>
          <Row className="text-sm">
            <strong>Reservation Placed</strong>
          </Row>
          <Row className="text-sm">
            <TextFormat type="date" value={props.reservation.createdDate} format={APP_DATE_FORMAT_MM_DD_YYYY} />
          </Row>
        </Col>
        <Col>
          <Row className="text-sm">
            <strong>Total Units</strong>
          </Row>
          <Row className="text-sm">{props.reservation.tokens.length}</Row>
        </Col>
        <Col>
          <Row className="text-sm">
            <strong>Total Price</strong>
          </Row>
          <Row className="text-sm">{formatPrice(props.reservation.totalPrice)}</Row>
        </Col>
        <Col>
          <Row className="text-sm">
            <strong>Buyer</strong>
          </Row>
          <Row className="text-sm">{props.reservation.buyer && props.reservation.buyer.login}</Row>
        </Col>
        <Col>
          <p className="text-sm">Reservation Number: {props.reservation.reservationNumber}</p>
        </Col>
      </Row>
    </CardHeader>
    <CardBody>
      <Row>
        <Col>
          <Link to={`/items/detail/${props.reservation.item.id}`}>
            <Row>{props.reservation.item.name}</Row>
            <Row>
              <img
                loading="lazy"
                className="responsive-img"
                height="50px"
                width="50px"
                src={
                  props.reservation.item.images && props.reservation.item.images.length > 0
                    ? props.reservation.item.images[0].imageUrl
                    : 'content/images/logo-gray.png'
                }
                alt="item image"
              />
            </Row>
          </Link>
        </Col>
        <Col>
          <Row>
            <strong>Event</strong>
          </Row>
          <Row>{props.reservation.event.eventName}</Row>
        </Col>
        <Col>
          <Row>
            <strong>Pickup Date</strong>
          </Row>
          <Row>
            <TextFormat type="date" value={props.reservation.pickupTime} format={APP_DATE_FORMAT_MM_DD_YYYY} />
          </Row>
        </Col>

        <Col className="float-right">
          {props.reservation.isActive && !props.reservation.isCancelled ? (
            <div className="btn-group-vertical float-right">
              <Button outline color="secondary" size="sm" onClick={() => props.onExchangeItem(props.reservation)}>
                Exchange
              </Button>
              <Button outline color="secondary" size="sm" onClick={() => props.onMessageBuyer(props.reservation)}>
                Message Seller
              </Button>
              <Button outline size="sm" color="danger" onClick={() => props.onCancelReservation(props.reservation)}>
                Cancel Reservation
              </Button>
            </div>
          ) : (
            <div className="text-center">
              <Row>
                <strong>Status</strong>
              </Row>
              <Row>
                {!props.reservation.isActive && !props.reservation.isCancelled && 'COMPLETE'}
                {!props.reservation.isActive && props.reservation.isCancelled && 'CANCELLED'}
              </Row>
            </div>
          )}
        </Col>
      </Row>
    </CardBody>
  </Card>
);

export default SellerReservationCard;

import React, { useState } from 'react';
import { Button, Col, Container, Input, Label, Modal, ModalBody, ModalFooter, ModalHeader, Row } from 'reactstrap';
import { IReservation } from 'app/shared/model/reservation.model';
import { TextFormat } from 'react-jhipster';
import { APP_DATE_FORMAT_MM_DD_YYYY } from 'app/config/constants';
import { Link } from 'react-router-dom';

interface CancelReservationDialogProps {
  reservation: IReservation;
  value: string;
  onChange: (msg: string) => void;
  isOpen: boolean;
  toggle: () => void;
  onSubmit: () => void;
}

export const CancelReservationDialog = (props: CancelReservationDialogProps) => {
  const [isErr, setIsErr] = useState(false);

  const onSubmit = () => {
    if (props.value === '') {
      setIsErr(true);
      return;
    }

    setIsErr(false);
    props.onSubmit();
  };

  return (
    <Modal toggle={props.toggle} isOpen={props.isOpen}>
      <ModalHeader toggle={props.toggle}>
        <strong>Cancel Reservation</strong>
      </ModalHeader>
      <ModalBody>
        <Container>
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
                <strong>Buyer</strong>
              </Row>
              <Row className="text-sm">{props.reservation.buyer.login}</Row>
            </Col>
            <Col>
              <p className="text-sm">Reservation Number: {props.reservation.reservationNumber}</p>
            </Col>
          </Row>
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
          </Row>
          <Row>
            <Label for="cancelMessage">
              <strong>Cancellation Message</strong>
            </Label>
            <Input value={props.value} onChange={event => props.onChange(event.target.value)} type="textarea" />
            {isErr && <div className="text-danger">Must Include a Message to Buyer</div>}
          </Row>
        </Container>
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={() => props.toggle()}>
          Cancel
        </Button>
        <Button color="primary" onClick={() => onSubmit()}>
          Cancel Reservation
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default CancelReservationDialog;

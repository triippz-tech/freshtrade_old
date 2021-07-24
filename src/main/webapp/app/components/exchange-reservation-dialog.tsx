import React, { useState } from 'react';
import { Button, Col, Container, Input, Label, Modal, ModalBody, ModalFooter, ModalHeader, Row } from 'reactstrap';
import { IReservation } from 'app/shared/model/reservation.model';
import { TextFormat } from 'react-jhipster';
import { APP_DATE_FORMAT_MM_DD_YYYY } from 'app/config/constants';
import { Link } from 'react-router-dom';
import { formatPrice } from 'app/shared/util/transform-utils';
import { QRCode } from 'react-qrcode-logo';

interface ExchangeReservationDialogProps {
  reservation: IReservation;
  isOpen: boolean;
  toggle: () => void;
}

export const ExchangeReservationDialog = (props: ExchangeReservationDialogProps) => {
  return (
    <Modal toggle={props.toggle} isOpen={props.isOpen}>
      <ModalHeader toggle={props.toggle}>
        <strong>Redeem Item: {props.reservation.item.name}</strong>
      </ModalHeader>
      <ModalBody>
        <Container>
          <Row>
            Please have &nbsp; <strong>{props.reservation.buyer.login}</strong> &nbsp; scan the following QR code to redeem item(s):
          </Row>
          <Row>
            Item: &nbsp;<strong>{props.reservation.item.name}</strong>
          </Row>
          <Row>
            Total Units for Buyer to Receive: &nbsp;<strong>{props.reservation.tokens.length}</strong>
          </Row>
          <Row>
            Total Price for Items: &nbsp;<strong>{formatPrice(props.reservation.totalPrice)}</strong>
          </Row>
          <br />
          <Row>
            <Col className="text-center">
              <QRCode
                value={`https://freshtrade.io/account/reservations/redeem/${props.reservation.reservationNumber}`}
                logoImage="content/images/freshtrade-logo.png"
                qrStyle="dots"
                logoOpacity={-1}
              />
            </Col>
          </Row>
        </Container>
      </ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={() => props.toggle()}>
          Ok
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default ExchangeReservationDialog;

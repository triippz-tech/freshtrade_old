import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './reservation.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IReservationDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ReservationDetail = (props: IReservationDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { reservationEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="reservationDetailsHeading">
          <Translate contentKey="freshtradeApp.reservation.detail.title">Reservation</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="freshtradeApp.reservation.id">Id</Translate>
            </span>
          </dt>
          <dd>{reservationEntity.id}</dd>
          <dt>
            <span id="reservationNumber">
              <Translate contentKey="freshtradeApp.reservation.reservationNumber">Reservation Number</Translate>
            </span>
          </dt>
          <dd>{reservationEntity.reservationNumber}</dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="freshtradeApp.reservation.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{reservationEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <span id="isCancelled">
              <Translate contentKey="freshtradeApp.reservation.isCancelled">Is Cancelled</Translate>
            </span>
          </dt>
          <dd>{reservationEntity.isCancelled ? 'true' : 'false'}</dd>
          <dt>
            <span id="cancellationNote">
              <Translate contentKey="freshtradeApp.reservation.cancellationNote">Cancellation Note</Translate>
            </span>
          </dt>
          <dd>{reservationEntity.cancellationNote}</dd>
          <dt>
            <span id="pickupTime">
              <Translate contentKey="freshtradeApp.reservation.pickupTime">Pickup Time</Translate>
            </span>
          </dt>
          <dd>
            {reservationEntity.pickupTime ? <TextFormat value={reservationEntity.pickupTime} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="freshtradeApp.reservation.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {reservationEntity.createdDate ? (
              <TextFormat value={reservationEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="updatedDate">
              <Translate contentKey="freshtradeApp.reservation.updatedDate">Updated Date</Translate>
            </span>
          </dt>
          <dd>
            {reservationEntity.updatedDate ? (
              <TextFormat value={reservationEntity.updatedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="freshtradeApp.reservation.seller">Seller</Translate>
          </dt>
          <dd>{reservationEntity.seller ? reservationEntity.seller.id : ''}</dd>
          <dt>
            <Translate contentKey="freshtradeApp.reservation.buyer">Buyer</Translate>
          </dt>
          <dd>{reservationEntity.buyer ? reservationEntity.buyer.id : ''}</dd>
          <dt>
            <Translate contentKey="freshtradeApp.reservation.event">Event</Translate>
          </dt>
          <dd>{reservationEntity.event ? reservationEntity.event.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/reservation" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/reservation/${reservationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ reservation }: IRootState) => ({
  reservationEntity: reservation.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ReservationDetail);

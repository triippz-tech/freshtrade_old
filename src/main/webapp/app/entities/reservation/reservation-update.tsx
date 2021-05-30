import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { setFileData, byteSize, Translate, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { ITradeEvent } from 'app/shared/model/trade-event.model';
import { getEntities as getTradeEvents } from 'app/entities/trade-event/trade-event.reducer';
import { getEntity, updateEntity, createEntity, setBlob, reset } from './reservation.reducer';
import { IReservation } from 'app/shared/model/reservation.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IReservationUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ReservationUpdate = (props: IReservationUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { reservationEntity, users, tradeEvents, loading, updating } = props;

  const { cancellationNote } = reservationEntity;

  const handleClose = () => {
    props.history.push('/reservation');
  };

  useEffect(() => {
    if (!isNew) {
      props.getEntity(props.match.params.id);
    }

    props.getUsers();
    props.getTradeEvents();
  }, []);

  const onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => props.setBlob(name, data, contentType), isAnImage);
  };

  const clearBlob = name => () => {
    props.setBlob(name, undefined, undefined);
  };

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.pickupTime = convertDateTimeToServer(values.pickupTime);
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.updatedDate = convertDateTimeToServer(values.updatedDate);

    if (errors.length === 0) {
      const entity = {
        ...reservationEntity,
        ...values,
        seller: users.find(it => it.id.toString() === values.sellerId.toString()),
        buyer: users.find(it => it.id.toString() === values.buyerId.toString()),
        event: tradeEvents.find(it => it.id.toString() === values.eventId.toString()),
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="freshtradeApp.reservation.home.createOrEditLabel" data-cy="ReservationCreateUpdateHeading">
            <Translate contentKey="freshtradeApp.reservation.home.createOrEditLabel">Create or edit a Reservation</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : reservationEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="reservation-id">
                    <Translate contentKey="freshtradeApp.reservation.id">Id</Translate>
                  </Label>
                  <AvInput id="reservation-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="reservationNumberLabel" for="reservation-reservationNumber">
                  <Translate contentKey="freshtradeApp.reservation.reservationNumber">Reservation Number</Translate>
                </Label>
                <AvField
                  id="reservation-reservationNumber"
                  data-cy="reservationNumber"
                  type="text"
                  name="reservationNumber"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    maxLength: { value: 100, errorMessage: translate('entity.validation.maxlength', { max: 100 }) },
                  }}
                />
              </AvGroup>
              <AvGroup check>
                <Label id="isActiveLabel">
                  <AvInput id="reservation-isActive" data-cy="isActive" type="checkbox" className="form-check-input" name="isActive" />
                  <Translate contentKey="freshtradeApp.reservation.isActive">Is Active</Translate>
                </Label>
              </AvGroup>
              <AvGroup check>
                <Label id="isCancelledLabel">
                  <AvInput
                    id="reservation-isCancelled"
                    data-cy="isCancelled"
                    type="checkbox"
                    className="form-check-input"
                    name="isCancelled"
                  />
                  <Translate contentKey="freshtradeApp.reservation.isCancelled">Is Cancelled</Translate>
                </Label>
              </AvGroup>
              <AvGroup>
                <Label id="cancellationNoteLabel" for="reservation-cancellationNote">
                  <Translate contentKey="freshtradeApp.reservation.cancellationNote">Cancellation Note</Translate>
                </Label>
                <AvInput id="reservation-cancellationNote" data-cy="cancellationNote" type="textarea" name="cancellationNote" />
              </AvGroup>
              <AvGroup>
                <Label id="pickupTimeLabel" for="reservation-pickupTime">
                  <Translate contentKey="freshtradeApp.reservation.pickupTime">Pickup Time</Translate>
                </Label>
                <AvInput
                  id="reservation-pickupTime"
                  data-cy="pickupTime"
                  type="datetime-local"
                  className="form-control"
                  name="pickupTime"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.reservationEntity.pickupTime)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="createdDateLabel" for="reservation-createdDate">
                  <Translate contentKey="freshtradeApp.reservation.createdDate">Created Date</Translate>
                </Label>
                <AvInput
                  id="reservation-createdDate"
                  data-cy="createdDate"
                  type="datetime-local"
                  className="form-control"
                  name="createdDate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.reservationEntity.createdDate)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="updatedDateLabel" for="reservation-updatedDate">
                  <Translate contentKey="freshtradeApp.reservation.updatedDate">Updated Date</Translate>
                </Label>
                <AvInput
                  id="reservation-updatedDate"
                  data-cy="updatedDate"
                  type="datetime-local"
                  className="form-control"
                  name="updatedDate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.reservationEntity.updatedDate)}
                />
              </AvGroup>
              <AvGroup>
                <Label for="reservation-seller">
                  <Translate contentKey="freshtradeApp.reservation.seller">Seller</Translate>
                </Label>
                <AvInput id="reservation-seller" data-cy="seller" type="select" className="form-control" name="sellerId">
                  <option value="" key="0" />
                  {users
                    ? users.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="reservation-buyer">
                  <Translate contentKey="freshtradeApp.reservation.buyer">Buyer</Translate>
                </Label>
                <AvInput id="reservation-buyer" data-cy="buyer" type="select" className="form-control" name="buyerId">
                  <option value="" key="0" />
                  {users
                    ? users.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="reservation-event">
                  <Translate contentKey="freshtradeApp.reservation.event">Event</Translate>
                </Label>
                <AvInput id="reservation-event" data-cy="event" type="select" className="form-control" name="eventId">
                  <option value="" key="0" />
                  {tradeEvents
                    ? tradeEvents.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/reservation" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  users: storeState.userManagement.users,
  tradeEvents: storeState.tradeEvent.entities,
  reservationEntity: storeState.reservation.entity,
  loading: storeState.reservation.loading,
  updating: storeState.reservation.updating,
  updateSuccess: storeState.reservation.updateSuccess,
});

const mapDispatchToProps = {
  getUsers,
  getTradeEvents,
  getEntity,
  updateEntity,
  setBlob,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ReservationUpdate);

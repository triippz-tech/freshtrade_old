import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './item.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IItemDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ItemDetail = (props: IItemDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { itemEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="itemDetailsHeading">
          <Translate contentKey="freshtradeApp.item.detail.title">Item</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="freshtradeApp.item.id">Id</Translate>
            </span>
          </dt>
          <dd>{itemEntity.id}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="freshtradeApp.item.price">Price</Translate>
            </span>
          </dt>
          <dd>{itemEntity.price}</dd>
          <dt>
            <span id="quantity">
              <Translate contentKey="freshtradeApp.item.quantity">Quantity</Translate>
            </span>
          </dt>
          <dd>{itemEntity.quantity}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="freshtradeApp.item.name">Name</Translate>
            </span>
          </dt>
          <dd>{itemEntity.name}</dd>
          <dt>
            <span id="details">
              <Translate contentKey="freshtradeApp.item.details">Details</Translate>
            </span>
          </dt>
          <dd>{itemEntity.details}</dd>
          <dt>
            <span id="itemCondition">
              <Translate contentKey="freshtradeApp.item.itemCondition">Item Condition</Translate>
            </span>
          </dt>
          <dd>{itemEntity.itemCondition}</dd>
          <dt>
            <span id="isActive">
              <Translate contentKey="freshtradeApp.item.isActive">Is Active</Translate>
            </span>
          </dt>
          <dd>{itemEntity.isActive ? 'true' : 'false'}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="freshtradeApp.item.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>{itemEntity.createdDate ? <TextFormat value={itemEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedDate">
              <Translate contentKey="freshtradeApp.item.updatedDate">Updated Date</Translate>
            </span>
          </dt>
          <dd>{itemEntity.updatedDate ? <TextFormat value={itemEntity.updatedDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="freshtradeApp.item.owner">Owner</Translate>
          </dt>
          <dd>{itemEntity.owner ? itemEntity.owner.id : ''}</dd>
          <dt>
            <Translate contentKey="freshtradeApp.item.location">Location</Translate>
          </dt>
          <dd>{itemEntity.location ? itemEntity.location.id : ''}</dd>
          <dt>
            <Translate contentKey="freshtradeApp.item.tradeEvent">Trade Event</Translate>
          </dt>
          <dd>{itemEntity.tradeEvent ? itemEntity.tradeEvent.id : ''}</dd>
          <dt>
            <Translate contentKey="freshtradeApp.item.categories">Categories</Translate>
          </dt>
          <dd>
            {itemEntity.categories
              ? itemEntity.categories.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {itemEntity.categories && i === itemEntity.categories.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="freshtradeApp.item.user">User</Translate>
          </dt>
          <dd>
            {itemEntity.users
              ? itemEntity.users.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {itemEntity.users && i === itemEntity.users.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}{' '}
          </dd>
        </dl>
        <Button tag={Link} to="/item" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/item/${itemEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ item }: IRootState) => ({
  itemEntity: item.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ItemDetail);

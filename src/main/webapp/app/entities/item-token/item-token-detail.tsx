import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './item-token.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IItemTokenDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ItemTokenDetail = (props: IItemTokenDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { itemTokenEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="itemTokenDetailsHeading">
          <Translate contentKey="freshtradeApp.itemToken.detail.title">ItemToken</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{itemTokenEntity.id}</dd>
          <dt>
            <span id="tokenName">
              <Translate contentKey="freshtradeApp.itemToken.tokenName">Token Name</Translate>
            </span>
          </dt>
          <dd>{itemTokenEntity.tokenName}</dd>
          <dt>
            <span id="tokenCode">
              <Translate contentKey="freshtradeApp.itemToken.tokenCode">Token Code</Translate>
            </span>
          </dt>
          <dd>{itemTokenEntity.tokenCode}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="freshtradeApp.itemToken.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {itemTokenEntity.createdDate ? <TextFormat value={itemTokenEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedDate">
              <Translate contentKey="freshtradeApp.itemToken.updatedDate">Updated Date</Translate>
            </span>
          </dt>
          <dd>
            {itemTokenEntity.updatedDate ? <TextFormat value={itemTokenEntity.updatedDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="freshtradeApp.itemToken.owner">Owner</Translate>
          </dt>
          <dd>
            {itemTokenEntity.owners
              ? itemTokenEntity.owners.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {itemTokenEntity.owners && i === itemTokenEntity.owners.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}{' '}
          </dd>
          <dt>
            <Translate contentKey="freshtradeApp.itemToken.item">Item</Translate>
          </dt>
          <dd>{itemTokenEntity.item ? itemTokenEntity.item.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/item-token" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/item-token/${itemTokenEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ itemToken }: IRootState) => ({
  itemTokenEntity: itemToken.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ItemTokenDetail);

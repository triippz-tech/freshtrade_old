import React, { useEffect, useState } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { IRootState } from 'app/shared/reducers';
import { getEntityDetail, reserveItems, reset } from 'app/entities/item/item.reducer';
import { connect } from 'react-redux';
import { Container, Row, Col, Breadcrumb, BreadcrumbItem, Form, Label, Button, Input, InputGroup, InputGroupAddon } from 'reactstrap';
import SwiperGallery from 'app/components/swiper-gallery';
import ProductBottomTabs from 'app/components/product-bottom-tabs';
import ReserveConfirmation from 'app/components/reserve-confirmation';
import SellerBlock from 'app/components/seller-block';
import NumberInput from 'app/components/number-input';

export interface ItemDetailAltDetail extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ItemDetailAlt = (props: ItemDetailAltDetail) => {
  const [alert, setAlert] = React.useState(true);
  const [reserveAmount, setReserveAmount] = useState<number>(0);

  useEffect(() => {
    props.getEntityDetail(props.match.params.id);
  }, []);

  const onChange = (value: number | string) => {
    if (typeof value === 'string') setReserveAmount(Number.parseInt(value, 2));
    else setReserveAmount(value);
  };

  const onSubmit = () => {
    props.reserveItems(itemEntity, reserveAmount);
  };

  const onKeyPress = e => {
    if (e.key === 'Enter') {
      const value = +e.target.value;
      if (isNaN(value)) return;
      if (value < 1 || value > itemEntity.quantity) return;
      onSubmit();
    }
  };

  const { itemEntity } = props;
  return (
    <React.Fragment>
      <section>
        <Container className="pt-5">
          <ReserveConfirmation isShowing={alert} setAlert={setAlert} productName={itemEntity.name} />

          <Breadcrumb>
            <BreadcrumbItem>
              <Link to="/">
                <a>Home</a>
              </Link>
            </BreadcrumbItem>
            <BreadcrumbItem>
              <Link to="/items">
                <a>Items</a>
              </Link>
            </BreadcrumbItem>
            <BreadcrumbItem active>{itemEntity.name}</BreadcrumbItem>
          </Breadcrumb>

          <Row>
            <Col lg="8" clacategoryssName="order-2 order-lg-1">
              <SwiperGallery images={itemEntity.images} />
            </Col>
            <Col lg="4" className="pl-lg-4 order-1 order-lg-2">
              <h1 className="h2 mb-4">{itemEntity && itemEntity.name}</h1>
              <div className="d-flex flex-column flex-sm-row align-items-sm-center justify-content-sm-between mb-4">
                <ul className="list-inline mb-2 mb-sm-0">
                  <li className="list-inline-item h4 font-weight-light mb-0">${itemEntity && itemEntity.price}</li>
                </ul>
              </div>
              <Form>
                <Row>
                  <Col sm="6" lg="12" className="detail-option mb-4">
                    <h6 className="detail-option-heading">Condition</h6>
                    <Label
                      className="btn btn-sm btn-outline-primary detail-option-btn-label"
                      tag="label"
                      for={itemEntity && itemEntity.itemCondition}
                    >
                      {' '}
                      {itemEntity && itemEntity.itemCondition}
                      <Input
                        className="input-invisible"
                        type="radio"
                        name="material"
                        value={itemEntity && itemEntity.itemCondition}
                        id={itemEntity && itemEntity.itemCondition}
                        disabled
                      />
                    </Label>
                  </Col>
                </Row>
                <InputGroup className="w-100 mb-4">
                  {props.isAuthenticated ? (
                    <>
                      <NumberInput
                        min={1}
                        className="detail-quantity"
                        max={itemEntity.availableQuantity}
                        onChange={value => onChange(value)}
                        onPressEnter={onKeyPress}
                        placeholder="Choose how many you'd like to reserve"
                        id={itemEntity.id}
                        name="items"
                        value={reserveAmount}
                        defaultValue={reserveAmount}
                      />
                      <InputGroupAddon addonType="append" className="flex-grow-1">
                        <Button color="dark" block onClick={() => onSubmit()}>
                          <i className="fa fa-shopping-cart mr-2"></i>Reserve Item(s)
                        </Button>
                      </InputGroupAddon>
                    </>
                  ) : (
                    <Col className="text-danger">
                      <Link to="/login">
                        <strong>Login</strong>
                      </Link>{' '}
                      or{' '}
                      <Link to="/account/register">
                        <strong>Register</strong>
                      </Link>{' '}
                      to reserve an item
                    </Col>
                  )}
                </InputGroup>
                <Row className="mb-4">
                  <Col xs="8">
                    {props.isAuthenticated && (
                      <Button size="sm" color="link">
                        <i className="far fa-envelope mr-2" />
                        Message Seller
                      </Button>
                    )}
                  </Col>
                  <Col xs="4" className="text-right">
                    <ul className="list-inline mb-0">
                      <li className="list-inline-item mr-2"></li>
                      <li className="list-inline-item"></li>
                    </ul>
                  </Col>
                </Row>
                <ul className="list-unstyled">
                  <li>
                    <strong>Categories:&nbsp;</strong>
                    {itemEntity &&
                      itemEntity.categories.map((category, index) => (
                        <React.Fragment key={category.name}>
                          <Link className="text-muted" to={`/items/${category.slug}`}>
                            {category.name}
                          </Link>
                          {index < itemEntity.categories.length - 1 ? ',\u00A0' : ''}
                        </React.Fragment>
                      ))}
                  </li>
                </ul>
              </Form>
              <hr />
              {itemEntity && itemEntity.owner && <SellerBlock user={itemEntity.owner} />}
            </Col>
          </Row>
        </Container>
      </section>
      <ProductBottomTabs item={itemEntity} />
    </React.Fragment>
  );
};

const mapStateToProps = ({ item, authentication }: IRootState) => ({
  itemEntity: item.entity,
  loading: item.loading,
  isAuthenticated: authentication.isAuthenticated,
});

const mapDispatchToProps = {
  getEntityDetail,
  reset,
  reserveItems,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ItemDetailAlt);

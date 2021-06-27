import React, { useEffect, useState } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { IRootState } from 'app/shared/reducers';
import { getEntityDetail, reset, reserveItems } from 'app/entities/item/item.reducer';
import { connect } from 'react-redux';
import './item-detail.scss';
import CustomMap from 'app/components/maps/custom-map';
import { toTitleCase } from 'app/shared/util/string-utils';
import { convertDateTimeFromServerToLocal } from 'app/shared/util/date-utils';
import NumberInput from 'app/components/number-input';
import {
  Button,
  Card,
  Carousel,
  CarouselCaption,
  CarouselControl,
  CarouselIndicators,
  CarouselItem,
  Col,
  Container,
  Row,
} from 'reactstrap';

export interface IItemDetail extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ItemDetail = (props: IItemDetail) => {
  const [reserveAmount, setReserveAmount] = useState<number>(0);
  const [activeIndex, setActiveIndex] = useState(0);
  const [animating, setAnimating] = useState(false);

  useEffect(() => {
    props.getEntityDetail(props.match.params.id);
  }, []);

  const onChange = (value: number | string) => {
    if (typeof value === 'string') setReserveAmount(Number.parseInt(value, 2));
    else setReserveAmount(value);
  };

  const onSubmit = () => {};

  const onKeyPress = e => {
    // if (e.key === 'Enter') {
    //   const value = +e.target.value;
    //   if (isNaN(value)) return;
    //   if (value < 1 || value > itemEntity.quantity) return;
    //   onSubmit();
    // }
  };

  const getItems = (): { src: string; altText: string; caption: string }[] => {
    if (props.itemEntity.images.length === 0)
      return [
        {
          src: 'content/images/logo-gray.png',
          altText: 'Default Image',
          caption: '',
        },
      ];

    return props.itemEntity.images.map(value => {
      if (value.isVisible)
        return {
          src: value.imageUrl,
          altText: value.id,
          caption: '',
        };
    });
  };

  const next = () => {
    if (animating) return;
    const nextIndex = activeIndex === getItems().length - 1 ? 0 : activeIndex + 1;
    setActiveIndex(nextIndex);
  };

  const previous = () => {
    if (animating) return;
    const nextIndex = activeIndex === 0 ? getItems().length - 1 : activeIndex - 1;
    setActiveIndex(nextIndex);
  };

  const goToIndex = newIndex => {
    if (animating) return;
    setActiveIndex(newIndex);
  };

  const slides = getItems().map(item => {
    return (
      <CarouselItem onExiting={() => setAnimating(true)} onExited={() => setAnimating(false)} key={item.src}>
        <img src={item.src} alt={item.altText} className="carousel-img" loading="lazy" />
        <CarouselCaption captionText={item.caption} captionHeader={item.caption} />
      </CarouselItem>
    );
  });

  const { itemEntity } = props;

  return (
    <Container fluid>
      <Card className="mb-5">
        <Container fluid>
          <Row className="">
            <Col md="8">
              <Carousel activeIndex={activeIndex} next={next} previous={previous} className="z-depth-1">
                <CarouselIndicators items={getItems()} activeIndex={activeIndex} onClickHandler={goToIndex} />
                {slides}
                <CarouselControl direction="prev" directionText="Previous" onClickHandler={previous} />
                <CarouselControl direction="next" directionText="Next" onClickHandler={next} />
              </Carousel>
            </Col>
            <Col md="4" className="details">
              <h3 className="product-title">{itemEntity.name}</h3>
              <h4 className="price">
                Price: <span>${itemEntity.price}</span>
              </h4>
              <h5 className="conditions">
                Condition:
                <span className="condition" data-toggle="tooltip" title="Condition">
                  {itemEntity.itemCondition && toTitleCase(itemEntity.itemCondition)}
                </span>
              </h5>
              <p className="seller">
                <strong>Seller:</strong> {itemEntity.owner && itemEntity.owner.login}
              </p>
              <p className="event">
                <strong>Event:</strong> {itemEntity.tradeEvent && itemEntity.tradeEvent.eventName}
              </p>
              <p className="event">
                <strong>Start Date:</strong> {itemEntity.tradeEvent && convertDateTimeFromServerToLocal(itemEntity.tradeEvent.startDate)}
              </p>
              <p className="event">
                <strong>End Date:</strong> {itemEntity.tradeEvent && convertDateTimeFromServerToLocal(itemEntity.tradeEvent.endDate)}
              </p>
              <p className="event">
                <strong>Address:</strong> {itemEntity.tradeEvent && itemEntity.tradeEvent.location.address}
              </p>
              {props.isAuthenticated ? (
                <div className="action">
                  <label htmlFor="number-input">Enter Quantity</label>
                  <NumberInput
                    min={1}
                    max={itemEntity.availableQuantity}
                    onChange={value => onChange(value)}
                    onPressEnter={onKeyPress}
                    placeholder="Choose how many you'd like to reserve"
                    id={itemEntity.id}
                    name="number-input"
                    value={reserveAmount}
                    defaultValue={reserveAmount}
                  />
                  <Button className="add-to-cart btn-ft-primary" onClick={() => onSubmit()}>
                    Reserve
                  </Button>
                </div>
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
            </Col>
          </Row>
          <hr />
          <Row>
            <h4>
              <strong>Description</strong>
            </h4>
            <p className="product-description">{itemEntity.details && itemEntity.details}</p>
          </Row>
          <hr />
          <Row>
            <h4>
              <strong>Location</strong>
            </h4>
            {itemEntity.tradeEvent ? (
              <CustomMap
                key={itemEntity.tradeEvent.location.id}
                lat={itemEntity.tradeEvent.location.latLocation}
                lng={itemEntity.tradeEvent.location.longLocation}
                popupText={itemEntity.tradeEvent.location.address}
              />
            ) : (
              <CustomMap popupText="Location is not specified" />
            )}
          </Row>
        </Container>
      </Card>
    </Container>
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

export default connect(mapStateToProps, mapDispatchToProps)(ItemDetail);

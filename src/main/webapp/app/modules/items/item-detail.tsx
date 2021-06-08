import React, { useEffect, useState } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { IRootState } from 'app/shared/reducers';
import { getEntityDetail, reset, reserveItems } from 'app/entities/item/item.reducer';
import { connect } from 'react-redux';
import { MDBBtn, MDBCard, MDBCarousel, MDBCarouselInner, MDBCarouselItem, MDBCol, MDBContainer, MDBRow, MDBView } from 'mdbreact';
import './item-detail.scss';
import CustomMap from 'app/components/maps/custom-map';
import { toTitleCase } from 'app/shared/util/string-utils';
import { convertDateTimeFromServerToLocal } from 'app/shared/util/date-utils';
import NumberInput from 'app/components/number-input';

export interface IItemDetail extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ItemDetail = (props: IItemDetail) => {
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
    <MDBContainer fluid>
      <MDBCard className="mb-5">
        <MDBContainer fluid>
          <MDBRow className="">
            <MDBCol md="6">
              <MDBCarousel
                activeItem={1}
                length={itemEntity.images.length === 0 ? 1 : itemEntity.images.length}
                showControls={true}
                showIndicators={true}
                className="z-depth-1"
              >
                <MDBCarouselInner>
                  {itemEntity.images.length === 0 && (
                    <MDBCarouselItem itemId="1" key={`tempimage-${itemEntity.id}`}>
                      <MDBView>
                        <img className="d-block img-fluid" src="content/images/logo-gray.png" alt="First slide" height="75%" />
                      </MDBView>
                    </MDBCarouselItem>
                  )}
                  {itemEntity.images.map((image, idx) => {
                    if (image.isVisible)
                      return (
                        <MDBCarouselItem itemId={`${idx + 1}`} key={idx}>
                          <MDBView>
                            <img className="d-block w-100" src={image.imageUrl} alt={image.id} />
                          </MDBView>
                        </MDBCarouselItem>
                      );
                  })}
                </MDBCarouselInner>
              </MDBCarousel>
            </MDBCol>
            <MDBCol md="6" className="details">
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
                  <MDBBtn className="add-to-cart btn-ft-primary" onClick={() => onSubmit()}>
                    Reserve
                  </MDBBtn>
                </div>
              ) : (
                <MDBCol className="text-danger">
                  <Link to="/login">
                    <strong>Login</strong>
                  </Link>{' '}
                  or{' '}
                  <Link to="/account/register">
                    <strong>Register</strong>
                  </Link>{' '}
                  to reserve an item
                </MDBCol>
              )}
            </MDBCol>
          </MDBRow>
          <hr />
          <MDBRow>
            <h4>
              <strong>Description</strong>
            </h4>
            <p className="product-description">{itemEntity.details && itemEntity.details}</p>
          </MDBRow>
          <hr />
          <MDBRow>
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
          </MDBRow>
        </MDBContainer>
      </MDBCard>
    </MDBContainer>
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

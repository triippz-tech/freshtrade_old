import React, { useEffect } from 'react';
import { IRootState } from 'app/shared/reducers';
import { connect } from 'react-redux';
import { getSellerKPIs, getTopSellingItems } from 'app/modules/seller/seller-metrics.reducer';
import TopSellingItemsChart from 'app/components/charts/top-selling-items-chart';
import { Card, CardBody, CardHeader, Col, Container, Row } from 'reactstrap';
import './seller-dashboard.scss';
import TopSellingCard from 'app/components/seller/top-selling-card';
import KpiCard from 'app/components/seller/kpi-card';
import { formatPrice } from 'app/shared/util/transform-utils';

interface SellerDashboardProps extends StateProps, DispatchProps {}

export const SellerDashboard = (props: SellerDashboardProps) => {
  useEffect(() => {
    props.getTopSellingItems();
    props.getSellerKPIs();
  }, []);
  return (
    <Container fluid className="mb-5">
      <section>
        <Row>
          <Container>
            <Card className="w-100 h-100">
              <CardHeader className="py-5">
                <h5 className="mb-0 text-center">
                  <strong>Sales Performance KPIs</strong>
                </h5>
              </CardHeader>
              <CardBody>
                <Row>
                  <Col className="col-xl-6 col-sm-6 col-12 mb-4">
                    {/* Total Revenue */}
                    <KpiCard
                      icon="arrow-left"
                      cardData={formatPrice(props.sellerKPIs.totalRevenue)}
                      cardTitle={'Total Revenue'}
                      iconColor={'red'}
                    />
                  </Col>
                  <Col className="col-xl-6 col-sm-6 col-12 mb-4">
                    {/* Monthly Revenue */}
                    <KpiCard
                      icon="arrow-left"
                      cardData={formatPrice(props.sellerKPIs.monthlyRevenue)}
                      cardTitle={'Monthly Revenue'}
                      iconColor={'text-danger'}
                    />
                  </Col>
                  <Col className="col-xl-6 col-sm-6 col-12 mb-4">
                    {/* Unique Buyers */}
                    <KpiCard
                      icon="arrow-left"
                      cardData={props.sellerKPIs.uniqueBuyers}
                      cardTitle={'Unique Buyers'}
                      iconColor={'text-danger'}
                    />
                  </Col>
                  <Col className="col-xl-6 col-sm-6 col-12 mb-4">
                    {/* Returning Buyers */}
                    <KpiCard
                      icon="arrow-left"
                      cardData={props.sellerKPIs.returningBuyers}
                      cardTitle={'Returning Buyers'}
                      iconColor={'text-danger'}
                    />
                  </Col>
                </Row>

                <Row>
                  <Col className="col-xl-6 col-sm-6 col-12 mb-4">
                    {/* Pending 7D Reservations */}
                    <KpiCard
                      icon="arrow-left"
                      cardData={props.sellerKPIs.pending7DReservations}
                      cardTitle={'7 Day Reservations'}
                      iconColor={'text-danger'}
                    />
                  </Col>
                  <Col className="col-xl-6 col-sm-6 col-12 mb-4">
                    {/* Active Reservations */}
                    <KpiCard
                      icon="arrow-left"
                      cardData={props.sellerKPIs.totalActiveReservations}
                      cardTitle={'Active Reservations'}
                      iconColor={'text-danger'}
                    />
                  </Col>
                  <Col className="col-xl-6 col-sm-6 col-12 mb-4">
                    {/* Completed Reservations */}
                    <KpiCard
                      icon="arrow-left"
                      cardData={props.sellerKPIs.totalCompletedReservations}
                      cardTitle={'Completed Reservations'}
                      iconColor={'text-danger'}
                    />
                  </Col>
                  <Col className="col-xl-6 col-sm-6 col-12 mb-4">
                    {/* Cancelled Orders */}
                    <KpiCard
                      icon="arrow-left"
                      cardData={props.sellerKPIs.totalCancellations}
                      cardTitle={'Cancelled Reservations'}
                      iconColor={'text-danger'}
                    />
                  </Col>
                </Row>
              </CardBody>
            </Card>
          </Container>
        </Row>
      </section>

      <section>
        <Row className="mb-4">
          <Container>
            <Card className="w-100 h-100">
              <CardHeader className="py-5">
                <h5 className="mb-0 text-center">
                  <strong>Top Items Sold</strong>
                </h5>
              </CardHeader>
              <CardBody>{!props.loadingTotalSold && <TopSellingItemsChart items={props.topSellingItems} />}</CardBody>
            </Card>
          </Container>
        </Row>
      </section>
    </Container>
  );
};

const mapStateToProps = ({ authentication, sellerMetrics }: IRootState) => ({
  isAuthenticated: authentication.isAuthenticated,
  topSellingItems: sellerMetrics.topSellingItems,
  sellerKPIs: sellerMetrics.sellerKPIs,
  loadingTotalSold: sellerMetrics.loadingTotalSold,
  loadingSellerKPIs: sellerMetrics.loadingKPIs,
});

const mapDispatchToProps = {
  getTopSellingItems,
  getSellerKPIs,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SellerDashboard);

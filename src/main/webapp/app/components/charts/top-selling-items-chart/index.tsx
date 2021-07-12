import React from 'react';
import { TopSellingItemsDTO } from 'app/shared/model/metrics/top-selling-items.dto';
import { HorizontalBar } from 'react-chartjs-2';
import { MDBSimpleChart } from 'mdbreact';

interface TopSellingItemsChartProps {
  items: ReadonlyArray<TopSellingItemsDTO>;
}

const Index = (props: TopSellingItemsChartProps) => {
  const transformData = () => {
    return props.items.map(value => {
      return {
        name: value.item.name,
        totalSold: value.totalSold,
      };
    });
  };

  return (
    <HorizontalBar
      type="bar"
      data={{
        labels: props.items.map(item => item.item.name),
        datasets: [
          {
            label: 'Top Selling Items',
            data: props.items.map(item => item.totalSold),
            backgroundColor: 'rgba(54, 162, 235, 0.2)',
            borderColor: 'rgb(153, 102, 255)',
            borderWidth: 1,
          },
        ],
      }}
      options={{
        indexAxis: 'y',
        scales: {
          x: {
            stacked: true,
            grid: {
              display: true,
              drawBorder: true,
            },
            ticks: {
              color: 'rgba(0,0,0, 0.5)',
            },
          },
          y: {
            stacked: true,
            grid: {
              display: false,
              drawBorder: false,
            },
            ticks: {
              color: 'rgba(0,0,0, 0.5)',
            },
          },
        },
      }}
    />
  );
};

export default Index;

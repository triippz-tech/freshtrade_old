function handleLocationError(error) {
  switch (error.code) {
    case error.PERMISSION_DENIED:
      alert('User denied the request for Geolocation.');
      break;
    case error.POSITION_UNAVAILABLE:
      alert('Location information is unavailable.');
      break;
    case error.TIMEOUT:
      alert('The request to get user location timed out.');
      break;
    case error.UNKNOWN_ERROR:
      alert('An unknown error occurred.');
      break;
    default:
      break;
  }
}

export function getCoordinates(position): any {
  const loc = {
    latitude: position.coords.latitude,
    longitude: position.coords.longitude,
  };
  fetch(
    `https://maps.googleapis.com/maps/api/geocode/json?latlng=${loc.latitude},${loc.longitude}&sensor=false&key=${process.env.APPLICATION_GOOGLE_MAPS_KEY}`
  )
    .then(response => response.json())
    .then(data => console.log(data))
    .catch(error => alert(error));
}

export function getLocation(): any {
  if (navigator.geolocation) {
    return navigator.geolocation.getCurrentPosition(getCoordinates, handleLocationError);
  }
}

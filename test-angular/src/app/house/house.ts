export class House  {
  id: number;
  ownerLogin: string;
  country: string;
  city: string;
  street: string;
  house: string;
  flat: string;


  constructor(id: number, ownerLogin: string, country: string, city: string, street: string, house: string, flat: string) {
    this.id = id;
    this.ownerLogin = ownerLogin;
    this.country = country;
    this.city = city;
    this.street = street;
    this.house = house;
    this.flat = flat;
  }
}

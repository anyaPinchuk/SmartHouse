import {User} from './user';

export class WorkLog {
  id: number;
  dateOfAction: Date;
  action: string;
  consumedEnergy: string;
  cost: string;
  hoursOfWork: string;
  user: User;
}

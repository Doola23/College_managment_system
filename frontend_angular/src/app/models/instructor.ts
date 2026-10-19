export interface InstructorDetails {
  id?: number;
  age?: number | null;
  email?: string | null;
  graduatedCollege?: string | null;
}

export interface Instructor {
  id?: number;
  firstName: string;
  lastName: string;
  courseId?: number | null;
  
  // Flat properties returned by InstructorResponseDto
  age?: number | null;
  email?: string | null;
  graduatedCollege?: string | null;

  // Optional nested property in case an endpoint returns it
  instructorDetails?: InstructorDetails | null;
}
// Define your backend base URLs:
const AUTH_BASE_URL = 'http://localhost:8081';
const COURSE_BASE_URL = 'http://localhost:8082';
const ENROLLMENT_BASE_URL = 'http://localhost:8083'; // adjust if necessary

/**
 * Helper function to get the JWT token from localStorage.
 */
function getToken() {
    return localStorage.getItem('jwt');
}

/**
 * Example function: Login
 */
export async function login(username, password) {
    const response = await fetch(`${AUTH_BASE_URL}/api/auth/login`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ username, password })
    });
    if (!response.ok) {
        throw new Error('Login failed');
    }
    return await response.json(); // Expected to return an object including the JWT token
}

/**
 * Example function: Fetch Courses
 */
export async function fetchCourses() {
    const response = await fetch(`${COURSE_BASE_URL}/courses`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${getToken()}`,
            'Content-Type': 'application/json'
        }
    });
    if (!response.ok) {
        throw new Error('Unable to fetch courses');
    }
    return await response.json();
}

/**
 * Example function: Create a Course (restricted operation)
 */
export async function createCourse(courseData) {
    const response = await fetch(`${COURSE_BASE_URL}/courses`, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${getToken()}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(courseData)
    });
    if (!response.ok) {
        throw new Error('Failed to create course');
    }
    return await response.json();
}

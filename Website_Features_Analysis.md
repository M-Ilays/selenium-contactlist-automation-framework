# Contact List App - Features & Functionalities Analysis

**Website URL:** https://thinking-tester-contact-list.herokuapp.com/login  
**API Documentation:** https://documenter.getpostman.com/view/4012288/TzK2bEa8

---

## 📋 APPLICATION OVERVIEW
A contact management web application built for testing purposes that allows users to manage their personal contact lists.

---

## 🔐 USER MANAGEMENT FEATURES

### 1. User Registration
- **Page:** Sign Up
- **Fields:**
  - Email (required)
  - Password (required)
  - First Name
  - Last Name
- **Functionality:** Creates a new user account
- **API Endpoint:** `POST /users`

### 2. User Login
- **Page:** Login Page
- **Fields:**
  - Email (ID: `email`)
  - Password (ID: `password`)
- **Button:** Submit (ID: `submit`)
- **Functionality:** Authenticates user and provides access token
- **API Endpoint:** `POST /users/login`
- **Success Action:** Redirects to `/contactList` page

### 3. User Profile Management
- **Get User Profile:** `GET /users/me`
- **Update User Profile:** `PATCH /users/me`
- **Delete User Account:** `DELETE /users/me`
- **Logout User:** `POST /users/logout`

---

## 👥 CONTACT MANAGEMENT FEATURES

### 1. Add Contact
- **Functionality:** Create a new contact with detailed information
- **API Endpoint:** `POST /contacts`
- **Required Headers:** `Authorization: Bearer {token}`
- **Contact Fields:**
  - First Name (required)
  - Last Name (required)
  - Birthdate (format: YYYY-MM-DD)
  - Email
  - Phone
  - Street 1
  - Street 2
  - City
  - State/Province
  - Postal Code
  - Country

**Example Contact Data:**
```json
{
    "firstName": "John",
    "lastName": "Doe",
    "birthdate": "1970-01-01",
    "email": "jdoe@fake.com",
    "phone": "8005555555",
    "street1": "1 Main St.",
    "street2": "Apartment A",
    "city": "Anytown",
    "stateProvince": "KS",
    "postalCode": "12345",
    "country": "USA"
}
```

### 2. View Contact List
- **Functionality:** Display all contacts for the logged-in user
- **API Endpoint:** `GET /contacts`
- **Required Headers:** `Authorization: Bearer {token}`
- **Page:** `/contactList`
- **Returns:** Array of contact objects with all details

### 3. View Single Contact
- **Functionality:** Get detailed information about a specific contact
- **API Endpoint:** `GET /contacts/:id`
- **Required Headers:** `Authorization: Bearer {token}`
- **Returns:** Complete contact details

### 4. Update Contact
- **Functionality:** Modify existing contact information
- **API Endpoint:** `PUT /contacts/:id` or `PATCH /contacts/:id`
- **Required Headers:** `Authorization: Bearer {token}`
- **Update Type:** Full update (PUT) or Partial update (PATCH)

### 5. Delete Contact
- **Functionality:** Remove a contact from the list
- **API Endpoint:** `DELETE /contacts/:id`
- **Required Headers:** `Authorization: Bearer {token}`

---

## 🔄 APPLICATION WORKFLOW

### **User Journey:**

1. **New User Registration Flow:**
   ```
   Landing Page → Click "Sign Up" Link → 
   Fill Registration Form → Submit → 
   Account Created → Redirect to Login
   ```

2. **Login Flow:**
   ```
   Login Page → Enter Email & Password → 
   Click Submit → Authentication → 
   Receive Token → Redirect to Contact List Page
   ```

3. **Contact Management Flow:**
   ```
   Contact List Page → Add New Contact → 
   Fill Contact Form → Save → 
   Contact Added to List → 
   View/Edit/Delete Options Available
   ```

4. **View/Edit Contact Flow:**
   ```
   Contact List → Select Contact → 
   View Details → Edit Button → 
   Modify Fields → Save Changes → 
   Updated Contact Displayed
   ```

5. **Delete Contact Flow:**
   ```
   Contact List → Select Contact → 
   Delete Button → Confirmation → 
   Contact Removed from List
   ```

6. **Logout Flow:**
   ```
   Any Page → Logout Button → 
   Session Terminated → Redirect to Login
   ```

---

## 🔑 AUTHENTICATION & SECURITY

- **Authentication Type:** Bearer Token Authentication
- **Token Usage:** Required for all contact operations
- **Authorization Header:** `Authorization: Bearer {token}`
- **Session Management:** Token-based authentication
- **User Isolation:** Users can only access their own contacts

---

## 📊 DATA STRUCTURE

### User Object:
```json
{
    "_id": "unique_user_id",
    "firstName": "string",
    "lastName": "string", 
    "email": "string",
    "password": "hashed_password"
}
```

### Contact Object:
```json
{
    "_id": "unique_contact_id",
    "firstName": "string",
    "lastName": "string",
    "birthdate": "YYYY-MM-DD",
    "email": "string",
    "phone": "string",
    "street1": "string",
    "street2": "string",
    "city": "string",
    "stateProvince": "string",
    "postalCode": "string",
    "country": "string",
    "owner": "user_id",
    "__v": 0
}
```

---

## 🎯 KEY FEATURES FOR TEST AUTOMATION

### Priority 1 - Core Functionality:
1. ✅ User Registration
2. ✅ User Login (Already Implemented)
3. ✅ Add Contact
4. ✅ View Contact List
5. ✅ View Single Contact
6. ✅ Update Contact
7. ✅ Delete Contact

### Priority 2 - User Management:
8. Update User Profile
9. Logout
10. Delete User Account

### Priority 3 - Validation & Error Handling:
11. Login with invalid credentials
12. Registration with duplicate email
13. Add contact with missing required fields
14. Update non-existent contact
15. Delete non-existent contact
16. Unauthorized access attempts

---

## 🧪 TEST SCENARIOS TO IMPLEMENT

### User Management Tests:
- ✅ Valid login with correct credentials
- Invalid login with wrong password
- Invalid login with non-existent email
- User registration with valid data
- User registration with existing email
- Update user profile
- Logout functionality

### Contact Management Tests:
- Add contact with all fields
- Add contact with only required fields
- View all contacts in list
- View single contact details
- Update contact information
- Delete a contact
- Search/Filter contacts (if available)

### Data Validation Tests:
- Invalid email format
- Invalid phone format
- Invalid date format for birthdate
- Missing required fields
- Empty form submission
- Special characters handling

### Security Tests:
- Access without authentication
- Access other user's contacts
- Token expiration handling
- SQL injection prevention
- XSS prevention

---

## 📝 NOTES

- **Database:** MongoDB (based on _id and __v fields)
- **Testing Purpose:** Database is purged periodically
- **API-First Design:** RESTful API with full CRUD operations
- **Responsive UI:** Web-based interface
- **Token Expiration:** Need to verify token lifetime

---

## 🚀 RECOMMENDED AUTOMATION STRUCTURE

```
pages/
├── LoginPage.java (✅ Implemented)
├── SignUpPage.java
├── ContactListPage.java
├── AddContactPage.java
├── EditContactPage.java
└── UserProfilePage.java

tests/
├── LoginTest.java (✅ Implemented)
├── SignUpTest.java
├── ContactCRUDTest.java
├── UserProfileTest.java
└── NegativeTest.java

utils/
├── BaseClass.java (✅ Implemented)
├── ExcelUtils.java (✅ Implemented)
├── APIUtils.java (To be added)
└── TestDataGenerator.java (To be added)
```

---

**Created:** February 7, 2026  
**Framework:** Selenium + Java + TestNG + Maven (Hybrid POM + Data-Driven)

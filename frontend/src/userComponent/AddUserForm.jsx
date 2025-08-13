// import { useState } from "react";
// import "react-toastify/dist/ReactToastify.css";
// import { ToastContainer, toast } from "react-toastify";
// import { useNavigate } from "react-router-dom";

// const AddUserForm = () => {
//   const navigate = useNavigate();

//   const [user, setUser] = useState({
//     firstName: "",
//     lastName: "",
//     emailId: "",
//     password: "",
//     phoneNo: "",
//     street: "",
//     city: "",
//     pincode: "",
//     role: "",
//   });

//   if (document.URL.indexOf("admin") != -1) {
//     user.role = "Admin";
//   } else if (document.URL.indexOf("delivery") != -1) {
//     user.role = "Delivery";
//   } else if (document.URL.indexOf("customer") != -1) {
//     user.role = "Customer";
//   } else if (document.URL.indexOf("supplier") != -1) {
//     user.role = "Supplier";
//   }

//   const handleUserInput = (e) => {
//     setUser({ ...user, [e.target.name]: e.target.value });
//   };

//   const saveUser = (event) => {
//     event.preventDefault();

//     if (user.emailId === "") {
//       toast.error("Enter Email Id", {
//         position: "top-center",
//         autoClose: 1000,
//         hideProgressBar: false,
//         closeOnClick: true,
//         pauseOnHover: true,
//         draggable: true,
//         progress: undefined,
//       });
//     } else if (user.password === "") {
//       toast.error("Enter Password", {
//         position: "top-center",
//         autoClose: 1000,
//         hideProgressBar: false,
//         closeOnClick: true,
//         pauseOnHover: true,
//         draggable: true,
//         progress: undefined,
//       });
//     } else if (user.phoneNo === "") {
//       toast.error("Enter Phone No", {
//         position: "top-center",
//         autoClose: 1000,
//         hideProgressBar: false,
//         closeOnClick: true,
//         pauseOnHover: true,
//         draggable: true,
//         progress: undefined,
//       });
//     } else if (user.role === "") {
//       toast.error("Role is not selected", {
//         position: "top-center",
//         autoClose: 1000,
//         hideProgressBar: false,
//         closeOnClick: true,
//         pauseOnHover: true,
//         draggable: true,
//         progress: undefined,
//       });
//     } else {
//       fetch("http://localhost:8080/api/user/register", {
//         method: "POST",
//         headers: {
//           Accept: "application/json",
//           "Content-Type": "application/json",
//         },
//         body: JSON.stringify(user),
//       }).then((result) => {
//         result.json().then((res) => {
//           if (res.success) {
//             toast.success(res.responseMessage, {
//               position: "top-center",
//               autoClose: 2000,
//               hideProgressBar: false,
//               closeOnClick: true,
//               pauseOnHover: true,
//               draggable: true,
//               progress: undefined,
//             });

//             setTimeout(() => {
//               navigate("/user/login");
//             }, 1000);
//           } else if (!res.success) {
//             toast.error(res.responseMessage, {
//               position: "top-center",
//               autoClose: 1000,
//               hideProgressBar: false,
//               closeOnClick: true,
//               pauseOnHover: true,
//               draggable: true,
//               progress: undefined,
//             });

//             setTimeout(() => {
//               window.location.reload(true);
//             }, 1000); // Redirect after 3 seconds
//           } else {
//             toast.error("It seems server is down", {
//               position: "top-center",
//               autoClose: 1000,
//               hideProgressBar: false,
//               closeOnClick: true,
//               pauseOnHover: true,
//               draggable: true,
//               progress: undefined,
//             });

//             setTimeout(() => {
//               window.location.reload(true);
//             }, 1000); // Redirect after 3 seconds
//           }
//         });
//       });
//     }
//   };

//   return (
//     <div>
//       <div class="mt-2 d-flex aligns-items-center justify-content-center ms-2 me-2 mb-2">
//         <div
//           class="card form-card border-color text-color custom-bg"
//           style={{ width: "50rem" }}
//         >
//           <div className="card-header bg-color custom-bg-text text-center">
//             <h5 class="card-title">Register {user.role}</h5>
//           </div>
//           <div class="card-body">
//             <form className="row g-3" onSubmit={saveUser}>
//               <div class="col-md-6 mb-3 text-color">
//                 <label for="title" class="form-label">
//                   <b> First Name</b>
//                 </label>
//                 <input
//                   type="text"
//                   class="form-control"
//                   id="firstName"
//                   name="firstName"
//                   onChange={handleUserInput}
//                   value={user.firstName}
//                   required
//                 />
//               </div>
//               <div class="col-md-6 mb-3 text-color">
//                 <label for="description" class="form-label">
//                   <b>Last Name</b>
//                 </label>
//                 <input
//                   type="text"
//                   class="form-control"
//                   id="lastName"
//                   name="lastName"
//                   onChange={handleUserInput}
//                   value={user.lastName}
//                   required
//                 />
//               </div>

//               <div className="col-md-6 mb-3 text-color">
//                 <b>
//                   <label className="form-label">Email Id</label>
//                 </b>
//                 <input
//                   type="email"
//                   class="form-control"
//                   id="emailId"
//                   name="emailId"
//                   onChange={handleUserInput}
//                   value={user.emailId}
//                   required
//                 />
//               </div>

//               <div class="col-md-6 mb-3">
//                 <b>
//                   <label for="quantity" class="form-label">
//                     Password
//                   </label>
//                 </b>
//                 <input
//                   type="password"
//                   class="form-control"
//                   id="password"
//                   name="password"
//                   onChange={handleUserInput}
//                   value={user.password}
//                   required
//                 />
//               </div>

//               <div class="col-md-6 mb-3">
//                 <label for="price" class="form-label">
//                   <b>Mobile No</b>
//                 </label>
//                 <input
//                   type="number"
//                   class="form-control"
//                   id="phoneNo"
//                   name="phoneNo"
//                   onChange={handleUserInput}
//                   value={user.phoneNo}
//                   required
//                 />
//               </div>

//               <div class="col-md-6 mb-3">
//                 <label for="description" class="form-label">
//                   <b> Street</b>
//                 </label>
//                 <textarea
//                   class="form-control"
//                   id="street"
//                   name="street"
//                   rows="3"
//                   onChange={handleUserInput}
//                   value={user.street}
//                   required
//                 />
//               </div>

//               <div class="col-md-6 mb-3">
//                 <label for="price" class="form-label">
//                   <b>City</b>
//                 </label>
//                 <input
//                   type="text"
//                   class="form-control"
//                   id="city"
//                   name="city"
//                   onChange={handleUserInput}
//                   value={user.city}
//                   required
//                 />
//               </div>

//               <div class="col-md-6 mb-3">
//                 <label for="pincode" class="form-label">
//                   <b>Pincode</b>
//                 </label>
//                 <input
//                   type="number"
//                   class="form-control"
//                   id="pincode"
//                   name="pincode"
//                   onChange={handleUserInput}
//                   value={user.pincode}
//                   required
//                 />
//               </div>

//               <div className="d-flex aligns-items-center justify-content-center">
//                 <input
//                   type="submit"
//                   className="btn bg-color custom-bg-text col-md-3"
//                   value="Register User"
//                 />
//               </div>
//               <ToastContainer />
//             </form>
//           </div>
//         </div>
//       </div>
//     </div>
//   );
// };

// export default AddUserForm;

import { useNavigate } from "react-router-dom";
import { useFormik } from "formik";
import * as Yup from "yup";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const AddUserForm = () => {
  const navigate = useNavigate();

  const getRoleFromURL = () => {
    if (document.URL.includes("admin")) return "Admin";
    if (document.URL.includes("delivery")) return "Delivery";
    if (document.URL.includes("customer")) return "Customer";
    if (document.URL.includes("supplier")) return "Supplier";
    return "";
  };

  const initialValues = {
    firstName: "",
    lastName: "",
    emailId: "",
    password: "",
    phoneNo: "",
    street: "",
    city: "",
    pincode: "",
    role: getRoleFromURL(),
  };

  const validationSchema = Yup.object({
    firstName: Yup.string().required("First name is required"),
    lastName: Yup.string().required("Last name is required"),
    emailId: Yup.string()
      .email("Invalid email format")
      .required("Email is required"),
    password: Yup.string()
      .min(6, "Password must be at least 6 characters")
      .required("Password is required"),
    phoneNo: Yup.string()
      .matches(/^[0-9]{10}$/, "Phone number must be 10 digits")
      .required("Phone number is required"),
    street: Yup.string().required("department name is required"),
    city: Yup.string()
      .matches(/^[A-Za-z ]+$/, "Office name should only contain letters")
      .required("office name is required"),
    pincode: Yup.string()
      .matches(/^[0-9]{6}$/, "department id must be 6 digits")
      .required("department id is required"),
    role: Yup.string().required("Role is required"),
  });

  const onSubmit = (values, { resetForm }) => {
    fetch("http://localhost:8080/api/user/register", {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(values),
    })
      .then((res) => res.json())
      .then((res) => {
        if (res.success) {
          toast.success(res.responseMessage, { position: "top-center", autoClose: 2000 });
          setTimeout(() => navigate("/user/login"), 1000);
        } else {
          toast.error(res.responseMessage || "Something went wrong", {
            position: "top-center",
            autoClose: 2000,
          });
        }
      })
      .catch(() => {
        toast.error("Server error", { position: "top-center", autoClose: 2000 });
      });
  };

  const formik = useFormik({ initialValues, validationSchema, onSubmit });

  return (
    <div className="mt-2 d-flex aligns-items-center justify-content-center ms-2 me-2 mb-2">
      <div className="card form-card border-color text-color custom-bg" style={{ width: "50rem" }}>
        <div className="card-header bg-color custom-bg-text text-center">
          <h5 className="card-title">Register </h5>
        </div>
        <div className="card-body">
          <form className="row g-3" onSubmit={formik.handleSubmit}>
            {[
              { label: "First Name", name: "firstName", type: "text" },
              { label: "Last Name", name: "lastName", type: "text" },
              { label: "Email Id", name: "emailId", type: "email" },
              { label: "Password", name: "password", type: "password" },
              { label: "Mobile No", name: "phoneNo", type: "text" },
              { label: "office name", name: "city", type: "text" },
              { label: "Department id", name: "pincode", type: "text" },
            ].map((field, index) => (
              <div key={index} className="col-md-6 mb-3 text-color">
                <label className="form-label"><b>{field.label}</b></label>
                <input
                  type={field.type}
                  className="form-control"
                  name={field.name}
                  onChange={formik.handleChange}
                  onBlur={formik.handleBlur}
                  value={formik.values[field.name]}
                />
                {formik.touched[field.name] && formik.errors[field.name] && (
                  <div className="text-danger">{formik.errors[field.name]}</div>
                )}
              </div>
            ))}

            <div className="col-md-6 mb-3">
              <label className="form-label"><b>Department name</b></label>
              <textarea
                className="form-control"
                rows="3"
                name="street"
                onChange={formik.handleChange}
                onBlur={formik.handleBlur}
                value={formik.values.street}
              />
              {formik.touched.street && formik.errors.street && (
                <div className="text-danger">{formik.errors.street}</div>
              )}
            </div>

            <div className="d-flex aligns-items-center justify-content-center">
              <button type="submit" className="btn bg-color custom-bg-text col-md-3">
                Register User
              </button>
            </div>
            <ToastContainer />
          </form>
        </div>
      </div>
    </div>
  );
};

export default AddUserForm;

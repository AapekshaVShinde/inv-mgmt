// import { useState } from "react";

// const AddDeliveryPerson = () => {
//   const [user, setUser] = useState({
//     firstName: "",
//     lastName: "",
//     emailId: "",
//     password: "",
//     phoneNo: "",
//     street: "",
//     city: "",
//     pincode: "",
//   });

//   const handleUserInput = (e) => {
//     setUser({ ...user, [e.target.name]: e.target.value });
//   };

//   const saveUser = () => {
//     fetch("http://localhost:8080/api/user/deliveryperson/register", {
//       method: "POST",
//       headers: {
//         Accept: "application/json",
//         "Content-Type": "application/json",
//       },
//       body: JSON.stringify(user),
//     }).then((result) => {
//       console.warn("result", result);
//       result.json().then((res) => {
//         console.log("response", res);
//       });
//     });
//   };

//   return (
//     <div>
//       <div class="mt-2 d-flex aligns-items-center justify-content-center ms-2 me-2 mb-2">
//         <div class="card form-card border-color" style={{ width: "25rem" }}>
//           <div className="card-header bg-color">
//             <h5 class="card-title">Add Delivery Person</h5>
//           </div>
//           <div class="card-body">
//             <form>
//               <div class="mb-3">
//                 <label for="title" class="form-label">
//                   First Name
//                 </label>
//                 <input
//                   type="text"
//                   class="form-control"
//                   id="firstName"
//                   name="firstName"
//                   onChange={handleUserInput}
//                   value={user.firstName}
//                 />
//               </div>
//               <div class="mb-3">
//                 <label for="description" class="form-label">
//                   Last Name
//                 </label>
//                 <input
//                   type="text"
//                   class="form-control"
//                   id="lastName"
//                   name="lastName"
//                   onChange={handleUserInput}
//                   value={user.lastName}
//                 />
//               </div>

//               <div className="mb-3">
//                 <label className="form-label">Email Id</label>
//                 <input
//                   type="email"
//                   class="form-control"
//                   id="emailId"
//                   name="emailId"
//                   onChange={handleUserInput}
//                   value={user.emailId}
//                 />
//               </div>

//               <div class="mb-3 mt-1">
//                 <label for="quantity" class="form-label">
//                   Password
//                 </label>
//                 <input
//                   type="password"
//                   class="form-control"
//                   id="password"
//                   name="password"
//                   onChange={handleUserInput}
//                   value={user.password}
//                 />
//               </div>

//               <div class="mb-3">
//                 <label for="price" class="form-label">
//                   Mobile No
//                 </label>
//                 <input
//                   type="number"
//                   class="form-control"
//                   id="phoneNo"
//                   name="phoneNo"
//                   onChange={handleUserInput}
//                   value={user.phoneNo}
//                 />
//               </div>

//               <div class="mb-3">
//                 <label for="description" class="form-label">
//                   Street
//                 </label>
//                 <textarea
//                   class="form-control"
//                   id="street"
//                   name="street"
//                   rows="3"
//                   onChange={handleUserInput}
//                   value={user.street}
//                 />
//               </div>

//               <div class="mb-3">
//                 <label for="price" class="form-label">
//                   City
//                 </label>
//                 <input
//                   type="text"
//                   class="form-control"
//                   id="city"
//                   name="city"
//                   onChange={handleUserInput}
//                   value={user.city}
//                 />
//               </div>

//               <div class="mb-3">
//                 <label for="pincode" class="form-label">
//                   Pincode
//                 </label>
//                 <input
//                   type="number"
//                   class="form-control"
//                   id="pincode"
//                   name="pincode"
//                   onChange={handleUserInput}
//                   value={user.pincode}
//                 />
//               </div>

//               <button
//                 type="submit"
//                 class="btn custom-bg text-color"
//                 onClick={saveUser}
//               >
//                 Register
//               </button>
//             </form>
//           </div>
//         </div>
//       </div>
//     </div>
//   );
// };

// export default AddDeliveryPerson;

import { useFormik } from "formik";
import * as Yup from "yup";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const AddDeliveryPerson = () => {
  const formik = useFormik({
    initialValues: {
      firstName: "",
      lastName: "",
      emailId: "",
      password: "",
      phoneNo: "",
      street: "",
      city: "",
      pincode: "",
    },
    validationSchema: Yup.object({
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
      street: Yup.string().required("Street is required"),
      city: Yup.string()
        .matches(/^[A-Za-z ]+$/, "City should only contain letters")
        .required("City is required"),
      pincode: Yup.string()
        .matches(/^[0-9]{6}$/, "Pincode must be 6 digits")
        .required("Pincode is required"),
    }),
    onSubmit: (values, { resetForm }) => {
      fetch("http://localhost:8080/api/user/deliveryperson/register", {
        method: "POST",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        body: JSON.stringify(values),
      })
        .then((result) => result.json())
        .then((res) => {
          if (res.success) {
            toast.success("Delivery Person Registered Successfully", {
              position: "top-center",
              autoClose: 2000,
            });
            resetForm();
          } else {
            toast.error(res.responseMessage || "Registration Failed", {
              position: "top-center",
              autoClose: 2000,
            });
          }
        })
        .catch(() => {
          toast.error("Server Error", {
            position: "top-center",
            autoClose: 2000,
          });
        });
    },
  });

  return (
    <div className="mt-2 d-flex aligns-items-center justify-content-center ms-2 me-2 mb-2">
      <div className="card form-card border-color" style={{ width: "25rem" }}>
        <div className="card-header bg-color">
          <h5 className="card-title">Add Delivery Person</h5>
        </div>
        <div className="card-body">
          <form onSubmit={formik.handleSubmit}>
            {[
              { label: "First Name", name: "firstName", type: "text" },
              { label: "Last Name", name: "lastName", type: "text" },
              { label: "Email Id", name: "emailId", type: "email" },
              { label: "Password", name: "password", type: "password" },
              { label: "Mobile No", name: "phoneNo", type: "text" },
              { label: "City", name: "city", type: "text" },
              { label: "Pincode", name: "pincode", type: "text" },
            ].map((field, index) => (
              <div key={index} className="mb-3">
                <label htmlFor={field.name} className="form-label">
                  {field.label}
                </label>
                <input
                  type={field.type}
                  className="form-control"
                  id={field.name}
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

            <div className="mb-3">
              <label htmlFor="street" className="form-label">
                Street
              </label>
              <textarea
                className="form-control"
                id="street"
                name="street"
                rows="3"
                onChange={formik.handleChange}
                onBlur={formik.handleBlur}
                value={formik.values.street}
              />
              {formik.touched.street && formik.errors.street && (
                <div className="text-danger">{formik.errors.street}</div>
              )}
            </div>

            <button type="submit" className="btn custom-bg text-color">
              Register
            </button>
            <ToastContainer />
          </form>
        </div>
      </div>
    </div>
  );
};

export default AddDeliveryPerson;

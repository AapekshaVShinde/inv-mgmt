// import { useState } from "react";
// import { ToastContainer, toast } from "react-toastify";
// import "react-toastify/dist/ReactToastify.css";
// import { useNavigate } from "react-router-dom";

// const UserLoginForm = () => {
//   let navigate = useNavigate();

//   const [loginRequest, setLoginRequest] = useState({
//     emailId: "",
//     password: "",
//     role: "",
//   });

//   const handleUserInput = (e) => {
//     setLoginRequest({ ...loginRequest, [e.target.name]: e.target.value });
//   };

//   const loginAction = (e) => {
//     if (loginRequest.role === "0" || loginRequest.role === "") {
//       toast.error("Select the Role", {
//         position: "top-center",
//         autoClose: 1000,
//         hideProgressBar: false,
//         closeOnClick: true,
//         pauseOnHover: true,
//         draggable: true,
//         progress: undefined,
//       });
//     } else {
//       fetch("http://localhost:8080/api/user/login", {
//         method: "POST",
//         headers: {
//           Accept: "application/json",
//           "Content-Type": "application/json",
//         },
//         body: JSON.stringify(loginRequest),
//       })
//         .then((result) => {
//           console.log("result", result);
//           result.json().then((res) => {
//             if (res.success) {
//               let userRes = res.users[0];

//               if (userRes.role === "Admin") {
//                 console.log("Working fine:)");
//                 sessionStorage.setItem("active-admin", JSON.stringify(userRes));
//               } else if (userRes.role === "Customer") {
//                 sessionStorage.setItem("active-user", JSON.stringify(userRes));
//               } else if (userRes.role === "Delivery") {
//                 sessionStorage.setItem(
//                   "active-delivery",
//                   JSON.stringify(userRes)
//                 );
//               }
//               toast.success(res.responseMessage, {
//                 position: "top-center",
//                 autoClose: 1000,
//                 hideProgressBar: false,
//                 closeOnClick: true,
//                 pauseOnHover: true,
//                 draggable: true,
//                 progress: undefined,
//               });
//               setTimeout(() => {
//                 window.location.href = "/home";
//               }, 1000); // Redirect after 3 seconds
//             } else {
//               toast.error(res.responseMessage, {
//                 position: "top-center",
//                 autoClose: 1000,
//                 hideProgressBar: false,
//                 closeOnClick: true,
//                 pauseOnHover: true,
//                 draggable: true,
//                 progress: undefined,
//               });
//             }
//           });
//         })
//         .catch((error) => {
//           console.error(error);
//           toast.error("It seems server is down", {
//             position: "top-center",
//             autoClose: 1000,
//             hideProgressBar: false,
//             closeOnClick: true,
//             pauseOnHover: true,
//             draggable: true,
//             progress: undefined,
//           });
//         });
//     }

//     e.preventDefault();
//   };

//   return (
//     <div>
//       <div className="mt-2 d-flex aligns-items-center justify-content-center">
//         <div
//           className="card form-card border-color custom-bg"
//           style={{ width: "25rem" }}
//         >
//           <div className="card-header bg-color text-center custom-bg-text">
//             <h4 className="card-title">User Login</h4>
//           </div>
//           <div className="card-body">
//             <form>
//               <div class="mb-3 text-color">
//                 <label for="role" class="form-label">
//                   <b>User Role</b>
//                 </label>
//                 <select
//                   onChange={handleUserInput}
//                   className="form-control"
//                   name="role"
//                   required
//                 >
//                   <option value="0">Select Role</option>
//                   <option value="Admin"> Admin </option>
//                   <option value="Customer"> Customer </option>
//                   <option value="Delivery"> Delivery Person </option>
//                 </select>
//               </div>

//               <div className="mb-3 text-color">
//                 <label for="emailId" class="form-label">
//                   <b>Email Id</b>
//                 </label>
//                 <input
//                   type="email"
//                   className="form-control"
//                   id="emailId"
//                   name="emailId"
//                   onChange={handleUserInput}
//                   value={loginRequest.emailId}
//                   required
//                 />
//               </div>
//               <div className="mb-3 text-color">
//                 <label for="password" className="form-label">
//                   <b>Password</b>
//                 </label>
//                 <input
//                   type="password"
//                   className="form-control"
//                   id="password"
//                   name="password"
//                   onChange={handleUserInput}
//                   value={loginRequest.password}
//                   required
//                 />
//               </div>
//               <button
//                 type="submit"
//                 className="btn bg-color custom-bg-text"
//                 onClick={loginAction}
//               >
//                 Login
//               </button>
//               <ToastContainer />
//             </form>
//           </div>
//         </div>
//       </div>
//     </div>
//   );
// };

// export default UserLoginForm;


import { useNavigate } from "react-router-dom";
import { ToastContainer, toast } from "react-toastify";
import { useFormik } from "formik";
import * as Yup from "yup";
import "react-toastify/dist/ReactToastify.css";

const UserLoginForm = () => {
  const navigate = useNavigate();

  const formik = useFormik({
    initialValues: {
      emailId: "",
      password: "",
      role: "0",
    },
    validationSchema: Yup.object({
      emailId: Yup.string()
        .email("Invalid email address")
        .required("Email is required"),
      password: Yup.string().required("Password is required"),
      role: Yup.string().notOneOf(["0", ""], "Please select a role"),
    }),
    onSubmit: (values) => {
      fetch("http://localhost:8080/api/user/login", {
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
            let userRes = res.users[0];

            if (userRes.role === "Admin") {
              sessionStorage.setItem("active-admin", JSON.stringify(userRes));
            } else if (userRes.role === "Customer") {
              sessionStorage.setItem("active-user", JSON.stringify(userRes));
            } else if (userRes.role === "Delivery") {
              sessionStorage.setItem("active-delivery", JSON.stringify(userRes));
            }

            toast.success(res.responseMessage, {
              position: "top-center",
              autoClose: 1000,
            });

            setTimeout(() => {
              window.location.href = "/home";
            }, 1000);
          } else {
            toast.error(res.responseMessage, {
              position: "top-center",
              autoClose: 1000,
            });
          }
        })
        .catch((error) => {
          console.error("Login error", error);
          toast.error("It seems server is down", {
            position: "top-center",
            autoClose: 1000,
          });
        });
    },
  });

  return (
    <div className="mt-2 d-flex aligns-items-center justify-content-center">
      <div className="card form-card border-color custom-bg" style={{ width: "25rem" }}>
        <div className="card-header bg-color text-center custom-bg-text">
          <h4 className="card-title">User Login</h4>
        </div>
        <div className="card-body">
          <form onSubmit={formik.handleSubmit}>
            {/* Role */}
            <div className="mb-3 text-color">
              <label htmlFor="role" className="form-label">
                <b>User Role</b>
              </label>
              <select
                name="role"
                className="form-control"
                onChange={formik.handleChange}
                onBlur={formik.handleBlur}
                value={formik.values.role}
              >
                <option value="0">Select Role</option>
                <option value="Admin">Admin</option>
                <option value="Customer">Employee</option>
                <option value="Delivery">Inventory head Person</option>
              </select>
              {formik.touched.role && formik.errors.role && (
                <div className="text-danger">{formik.errors.role}</div>
              )}
            </div>

            {/* Email */}
            <div className="mb-3 text-color">
              <label htmlFor="emailId" className="form-label">
                <b>Email Id</b>
              </label>
              <input
                type="email"
                name="emailId"
                className="form-control"
                onChange={formik.handleChange}
                onBlur={formik.handleBlur}
                value={formik.values.emailId}
              />
              {formik.touched.emailId && formik.errors.emailId && (
                <div className="text-danger">{formik.errors.emailId}</div>
              )}
            </div>

            {/* Password */}
            <div className="mb-3 text-color">
              <label htmlFor="password" className="form-label">
                <b>Password</b>
              </label>
              <input
                type="password"
                name="password"
                className="form-control"
                onChange={formik.handleChange}
                onBlur={formik.handleBlur}
                value={formik.values.password}
              />
              {formik.touched.password && formik.errors.password && (
                <div className="text-danger">{formik.errors.password}</div>
              )}
            </div>

            {/* Submit */}
            <button type="submit" className="btn bg-color custom-bg-text">
              Login
            </button>
            <ToastContainer />
          </form>
        </div>
      </div>
    </div>
  );
};

export default UserLoginForm;

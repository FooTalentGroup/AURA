import DashboardLayout from "../../layouts/DashboardLayout"


function AdminPage() {
  return (
    <DashboardLayout>
        <div className="container mx-auto px-4 py-4">
            <h1 className="text-2xl font-bold text-gray-800">Admin Page</h1>
            <p className="mt-2 text-gray-600">Welcome to the admin page!</p>
        </div>
    </DashboardLayout>
    
  )
}

export default AdminPage
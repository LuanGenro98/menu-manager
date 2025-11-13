import { SidebarDashboard } from "./_components/sidebar";

export default function DashBoardLayout({ children } : { children: React.ReactNode }){
  return (
    <>
     <SidebarDashboard>
        {children}
      </SidebarDashboard>
    </>
  )

}
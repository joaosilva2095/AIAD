package aiad.feup;

import aiad.feup.messages.JoinPlayer;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

public class PingPong extends Agent {

   class PingPongBehaviour extends SimpleBehaviour {
      private int n = 0;

      public PingPongBehaviour(Agent a) {
         super(a);
      }

      public void action() {
         ACLMessage msg = blockingReceive();
         if(msg.getPerformative() == ACLMessage.INFORM) {
            System.out.println(++n + " " + getLocalName() + ": recebi " + msg.getContent());
            // cria resposta
            ACLMessage reply = msg.createReply();
            // preenche conteudo da mensagem
            if(msg.getContent().equals("ping"))
               reply.setContent("pong");
            else reply.setContent("ping");
            // envia mensagem
            send(reply);
         }
      }

      public boolean done() {
         return n==10;
      }

   }

   protected void setup() {
      String tipo = "";
      Object[] args = getArguments();
      if(args != null && args.length > 0) {
         tipo = (String) args[0];
      } else {
         System.out.println("N�o especificou o tipo");
      }
      
      // regista agente no DF
      DFAgentDescription dfd = new DFAgentDescription();
      dfd.setName(getAID());
      ServiceDescription sd = new ServiceDescription();
      sd.setName(getName());
      sd.setType("Agente " + tipo);
      dfd.addServices(sd);
      try {
         DFService.register(this, dfd);
      } catch(FIPAException e) {
         e.printStackTrace();
      }

      PingPongBehaviour b = new PingPongBehaviour(this);
      addBehaviour(b);
	  
      // toma a iniciativa se for agente "pong"
      if(tipo.equals("pong")) {
         // pesquisa DF por agentes "ping"
         DFAgentDescription template = new DFAgentDescription();
         ServiceDescription sd1 = new ServiceDescription();
         sd1.setType("Agente ping");
         template.addServices(sd1);
         try {
            DFAgentDescription[] result = DFService.search(this, template);
            // envia mensagem "pong" inicial a todos os agentes "ping"
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            for(int i=0; i<result.length; ++i)
               msg.addReceiver(result[i].getName());
            msg.setContent("pong");
            send(msg);
         } catch(FIPAException e) { e.printStackTrace(); }
      }

   }

   protected void takeDown() {
      try {
         DFService.deregister(this);  
      } catch(FIPAException e) {
         e.printStackTrace();
      }
   }

}


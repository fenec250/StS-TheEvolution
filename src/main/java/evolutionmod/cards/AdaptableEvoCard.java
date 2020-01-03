package evolutionmod.cards;

import basemod.abstracts.CustomCard;
import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import evolutionmod.orbs.AbstractGene;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public abstract class AdaptableEvoCard extends CustomCard {

//	private List<AbstractAdaptation> adaptations;
	protected Map<String, AbstractAdaptation> adaptationMap;

    public AdaptableEvoCard(final String id, final String name, final String img, final int cost, final String rawDescription,
                            final CardType type, final CardColor color,
                            final CardRarity rarity, final CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
//	    this.adaptations = new LinkedList<>();
	    this.adaptationMap = new HashMap<>();
    }

    public int addAdaptation(AbstractAdaptation adaptation) {
//    	adaptations.add(adaptation);
    	if (this.adaptationMap.containsKey(adaptation.getGeneId())) {
    		this.adaptationMap.get(adaptation.getGeneId()).amount += adaptation.amount;
    		if (this.adaptationMap.get(adaptation.getGeneId()).amount <= 0) {
			    this.adaptationMap.remove(adaptation.getGeneId());
		    }
	    } else {
		    if (adaptationMap.isEmpty()) { // TODO this can trigger multiple times if something removes adaptations
			    this.name += "&";
			    this.rawDescription += " NL ";
		    }
    		this.adaptationMap.put(adaptation.getGeneId(), adaptation);
		    this.rawDescription += adaptation.text() + " "; //TODO: add a method to get the description segment?
		    this.initializeDescription();
	    }
	    return adaptation.amount;
    }

	@Override
	public AbstractCard makeCopy() {
		AdaptableEvoCard card = (AdaptableEvoCard) super.makeCopy();
//		card.adaptations.addAll(this.adaptations);
		card.adaptationMap.putAll(this.adaptationMap);
		return card;
	}

	protected void useAdaptations(AbstractPlayer p, AbstractMonster m) {
//    	adaptations.forEach(f -> f.apply(p, m));
    	this.adaptationMap.values().forEach(f -> f.apply(p, m));
    }

    protected boolean consumeOrbs(AbstractPlayer player, Collection<AbstractOrb> orbs) {
	    if (player.orbs.isEmpty()) {
	    	return false;
	    }
	    boolean result = player.orbs.removeAll(orbs);
	    if (result) {
		    for (int i = 0; i < orbs.size(); ++i) {
		    	player.orbs.add(new EmptyOrbSlot(((AbstractOrb)player.orbs.get(0)).cX, ((AbstractOrb)player.orbs.get(0)).cY));
		    }
		    for (int i = 0; i < player.orbs.size(); ++i) {
			    ((AbstractOrb)player.orbs.get(i)).setSlot(i, player.maxOrbs);
		    }
	    }
	    return result;
    }

    public abstract static class AbstractAdaptation {
    	public int amount;
    	public AbstractAdaptation(int amount) {
    		this.amount = amount;
	    }
	    public abstract void apply(AbstractPlayer player, AbstractMonster monster);
	    public abstract String text();
	    public abstract String getGeneId();
    }
//    public static class TempoNumber extends DynamicVariable {
//
//        @Override
//        public int baseValue(AbstractCard card) {
//            return ((AdaptableEvoCard) card).tempo;
//        }
//
//        @Override
//        public boolean isModified(AbstractCard card) {
//            return ((AdaptableEvoCard) card).tempoModified;
//        }
//
//        @Override
//        public String key() {
//            return "melodyMod:T";
//        }
//
//        @Override
//        public boolean upgraded(AbstractCard card) {
//            return ((AdaptableEvoCard) card).tempoUpgraded;
//        }
//
//        @Override
//        public int value(AbstractCard card) {
//            return ((AdaptableEvoCard) card).tempo;
//        }
//    }
}
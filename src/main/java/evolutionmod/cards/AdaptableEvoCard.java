package evolutionmod.cards;

import basemod.abstracts.CustomCard;
import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import evolutionmod.orbs.AbstractGene;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


public abstract class AdaptableEvoCard extends CustomCard {

	protected Map<String, AbstractAdaptation> adaptationMap;
	protected int adaptationMaximum = -1;
	protected boolean adaptationUpgraded = false;

    public AdaptableEvoCard(final String id, final String name, final String img, final int cost, final String rawDescription,
                            final CardType type, final CardColor color,
                            final CardRarity rarity, final CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
	    this.adaptationMap = new HashMap<>();
    }

    public int addAdaptation(AbstractGene gene) {
		int adapted = addAdaptation(gene.getAdaptation());
		if (adapted > 0) {
			consumeOrb(AbstractDungeon.player, gene);
		}
		return adapted;
	}

    public int addAdaptation(AbstractAdaptation adaptation) {
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
		card.adaptationMap = this.adaptationMap.values().stream()
				.map(AbstractAdaptation::makeCopy)
				.collect(Collectors.toMap(
						AbstractAdaptation::getGeneId,
						Function.identity(),
						(a, b) -> {a.amount += b.amount; return a;}));
		card.adaptationMaximum = this.adaptationMaximum;
		card.adaptationUpgraded = this.adaptationUpgraded;
		return card;
	}

	protected void useAdaptations(AbstractPlayer p, AbstractMonster m) {
    	this.adaptationMap.values().forEach(f -> f.apply(p, m));
    }

    protected boolean consumeOrb(AbstractPlayer player, AbstractOrb orb) {
	    if (player.orbs.isEmpty()) {
	    	return false;
	    }
	    boolean result = player.orbs.remove(orb);
	    if (result) {
		    player.orbs.add(new EmptyOrbSlot(player.orbs.get(0).cX, player.orbs.get(0).cY));
		    for (int i = 0; i < player.orbs.size(); ++i) {
			    player.orbs.get(i).setSlot(i, player.maxOrbs);
		    }
	    }
	    return result;
    }

    protected boolean consumeOrbs(AbstractPlayer player, Collection<AbstractOrb> orbs) {
	    if (player.orbs.isEmpty()) {
	    	return false;
	    }
	    boolean result = player.orbs.removeAll(orbs);
	    if (result) {
		    for (int i = 0; i < orbs.size(); ++i) {
		    	player.orbs.add(new EmptyOrbSlot(player.orbs.get(0).cX, player.orbs.get(0).cY));
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
		public abstract AbstractAdaptation makeCopy();
    }

    protected void upgradeAdaptationMaximum(int change) {
    	this.adaptationMaximum += change;
    	this.adaptationUpgraded = true;
	}

    public static class MaxAdaptationNumber extends DynamicVariable {

        @Override
        public int baseValue(AbstractCard card) {
            return ((AdaptableEvoCard) card).adaptationMaximum;
        }

		@Override
		public int value(AbstractCard card) {
			return ((AdaptableEvoCard) card).adaptationMaximum;
		}

        @Override
        public boolean isModified(AbstractCard card) {
        	return false;
        }

        @Override
        public String key() {
            return "evolutionmod:MA";
        }

        @Override
        public boolean upgraded(AbstractCard card) {
			return ((AdaptableEvoCard) card).adaptationUpgraded;
        }
    }
}
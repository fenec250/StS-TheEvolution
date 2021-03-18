package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.InsectGene;
import evolutionmod.patches.AbstractCardEnum;

import java.util.stream.Collectors;

public class SpiderBite
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:SpiderBite";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/SpiderBite.png";
    private static final int COST = 1;
    private static final int DAMAGE_AMT = 8;
//    private static final int UPGRADE_DAMAGE_AMT = 1;
    private static final int SPIDER_DAMAGE_AMT = 1;
    private static final int UPGRADE_SPIDER_DAMAGE_AMT = 1;

    public SpiderBite() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
        this.magicNumber = this.baseMagicNumber = SPIDER_DAMAGE_AMT;
		this.cardsToPreview = new Drone();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new DamageAction(
                m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_LIGHT));


		formEffect(InsectGene.ID, () -> addToBot(new AbstractGameAction() {
			@Override
			public void update() {
				CardGroup g = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
				Drone drone = Drone.createDroneWithInteractions(p);
				drone.target_x = drone.current_x = (float) Settings.WIDTH / 2.0F - 200.0F * Settings.scale;
				drone.target_y = drone.current_y = (float) Settings.HEIGHT / 2.0F;
				g.addToTop(drone);
				drone = Drone.createDroneWithInteractions(p);
				drone.target_x = drone.current_x = (float) Settings.WIDTH / 2.0F + 200.0F * Settings.scale;
				drone.target_y = drone.current_y = (float) Settings.HEIGHT / 2.0F;
				g.addToTop(drone);
				g.group.stream()
						.collect(Collectors.toList()) // solidify a list so we can remove from g
						.forEach(g::moveToExhaustPile);
				this.isDone = true;
			}
		}));

//		if (!AbstractGene.isPlayerInThisForm(LizardGene.ID)) {
//			AbstractDungeon.actionManager.addToBottom(new ChannelAction(new LizardGene()));
//		} else {
//			List<AbstractOrb> genes = p.orbs.stream()
//					.filter(o -> this.canAdaptWith(o) > 0)
////                        .findAny()
////                        .ifPresent(o -> this.tryAdaptingWith(o, true));
//					.collect(Collectors.toList());
//			genes.forEach(o -> this.tryAdaptingWith(o, true));
//			p.hand.group.stream()
//					.filter(card -> Drone.ID.equals(card.cardID))
//					.forEach(card -> {
//						AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
//								m, p, new PoisonPower(m, p, this.magicNumber), this.magicNumber, true));
//						addToBot(new ExhaustSpecificCardAction(card, p.hand, true));
//					});
////			this.useAdaptations(p, m);
//			for (int i = 0; i < this.adaptationMap.get(InsectGene.ID).amount; ++i) {
//				AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
//						m, p, new PoisonPower(m, p, this.magicNumber), this.magicNumber, true));
//			}
//		}
	}

	@Override
	public void applyPowers() {
		alterDamageAround(super::applyPowers);
	}

	@Override
	public void calculateCardDamage(AbstractMonster mo) {
		alterDamageAround(() -> super.calculateCardDamage(mo));
	}

	private void alterDamageAround(Runnable supercall) {
		this.baseDamage = DAMAGE_AMT;
//    	this.baseMagicNumber = DAMAGE_AMT + (this.upgraded ? UPGRADE_DAMAGE_AMT : 0);
//		int mn = magicNumber;
		this.baseDamage += this.magicNumber * AbstractDungeon.player.exhaustPile.group.stream()
				.filter(card -> Drone.ID.equals(card.cardID))
				.count();
		if (BaseEvoCard.isPlayerInThisForm(InsectGene.ID)) {
			this.baseDamage += 2;
//			this.baseDamage += this.magicNumber * AbstractDungeon.player.hand.group.stream()
//							.filter(card -> card instanceof AbstractDrone)
////							.filter(card -> Drone.ID.equals(card.cardID))
//							.count();
//			magicNumber = 0;
//			this.isMagicNumberModified = true;
		}
		supercall.run();
//		magicNumber = mn;
		this.baseDamage = DAMAGE_AMT;
		this.isDamageModified = this.damage != this.baseDamage;
	}

//	@Override
//	public int canAdaptWith(AbstractAdaptation adaptation) {
//		return adaptation.getGeneId().equals(InsectGene.ID) ? adaptation.amount : 0;
//	}

	@Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_SPIDER_DAMAGE_AMT);
        }
    }

	@Override
	public void triggerOnGlowCheck() {
		if (isPlayerInThisForm(InsectGene.ID)) {
			this.glowColor = InsectGene.COLOR.cpy();
		} else {
			this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
		}
	}
}
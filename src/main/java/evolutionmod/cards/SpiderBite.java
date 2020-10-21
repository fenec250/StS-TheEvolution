package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.PoisonPower;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.orbs.InsectGene;
import evolutionmod.orbs.LizardGene;
import evolutionmod.patches.AbstractCardEnum;

import java.util.List;
import java.util.stream.Collectors;

public class SpiderBite
        extends AdaptableEvoCard {
    public static final String ID = "evolutionmod:SpiderBite";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/InsectAtt.png";
    private static final int COST = 1;
    private static final int DAMAGE_AMT = 7;
    private static final int UPGRADE_DAMAGE_AMT = 1;
    private static final int LIZARD_POISON_AMT = 3;
    private static final int UPGRADE_LIZARD_POISON_AMT = 1;

    public SpiderBite() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
        this.magicNumber = this.baseMagicNumber = LIZARD_POISON_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new DamageAction(
                m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.BLUNT_LIGHT));


		if (!AbstractGene.isPlayerInThisForm(LizardGene.ID)) {
			AbstractDungeon.actionManager.addToBottom(new ChannelAction(new LizardGene()));
			this.useAdaptations(p, m);
		} else {
			List<AbstractOrb> genes = p.orbs.stream()
					.filter(o -> this.canAdaptWith(o) > 0)
//                        .findAny()
//                        .ifPresent(o -> this.tryAdaptingWith(o, true));
					.collect(Collectors.toList());
			genes.forEach(o -> this.tryAdaptingWith(o, true));
			p.hand.group.stream()
					.filter(card -> Drone.ID.equals(card.cardID))
					.forEach(card -> {
						AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
								m, p, new PoisonPower(m, p, this.magicNumber), this.magicNumber, true));
						addToBot(new ExhaustSpecificCardAction(card, p.hand, true));
					});
//			this.useAdaptations(p, m);
			for (int i = 0; i < this.adaptationMap.get(InsectGene.ID).amount; ++i) {
				AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(
						m, p, new PoisonPower(m, p, this.magicNumber), this.magicNumber, true));
			}
		}
	}

	@Override
	public int canAdaptWith(AbstractAdaptation adaptation) {
		return adaptation.getGeneId().equals(InsectGene.ID) ? adaptation.amount : 0;
	}

	@Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE_AMT);
            this.upgradeMagicNumber(UPGRADE_LIZARD_POISON_AMT);
        }
    }
}
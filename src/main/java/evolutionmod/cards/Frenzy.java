package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import evolutionmod.orbs.BeastGene2;
import evolutionmod.patches.EvolutionEnum;

public class Frenzy extends BaseEvoCard {
    public static final String ID = "evolutionmodV2:Frenzy";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/Frenzy.png";
    private static final int COST = 0;
    private static final int DAMAGE_AMT = 6;
    private static final int UPGRADE_DAMAGE_AMT = 4;
    private static final int POWER_AMT = 1;
    private static final int FORM_POWER_AMT = 1;

    public Frenzy() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.ATTACK, EvolutionEnum.EVOLUTION_V2_BLUE,
                CardRarity.RARE, CardTarget.ENEMY);
        this.damage = this.baseDamage = DAMAGE_AMT;
        this.magicNumber = this.baseMagicNumber = POWER_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(
                m, new DamageInfo(p, this.damage, this.damageTypeForTurn),
                AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        int power = this.magicNumber + (isPlayerInThisForm(BeastGene2.ID)?FORM_POWER_AMT:0);
        addToBot(new ApplyPowerAction(p, p, new FrenzyPower(p, power), power));
        formEffect(BeastGene2.ID);
    }

    @Override
    public AbstractCard makeCopy() {
        return new Frenzy();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE_AMT);
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if (isPlayerInThisForm(BeastGene2.ID)) {
            this.glowColor = BeastGene2.COLOR.cpy();
        } else {
            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }

    public static class FrenzyPower extends AbstractPower {
        public static final String POWER_ID = "evolutionmod:FrenzyPower";
        public static final PowerStrings cardStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
        public static final String NAME = cardStrings.NAME;
        public static final String[] DESCRIPTIONS = cardStrings.DESCRIPTIONS;

        public AbstractPlayer player;

        public FrenzyPower(AbstractPlayer player, int amount) {
            this.name = NAME;
            this.ID = POWER_ID;
            this.owner = player;
            this.player = player;
//            this.region128 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/AphrodisiacPower84.png"), 0, 0, 84, 84);
//            this.region48 = new TextureAtlas.AtlasRegion(new Texture("evolutionmod/images/powers/AphrodisiacPower32.png"), 0, 0, 32, 32);
            this.loadRegion("swivel");
            this.type = PowerType.BUFF;
            this.amount = amount;
            this.updateDescription();
        }

        @Override
        public void onUseCard(AbstractCard card, UseCardAction action) {
            int energy = card.costForTurn != -1 ? card.costForTurn
                    : card.cost > 0 ? card.cost
                    : card.cost == -1 ? card.energyOnUse
                    : 0;

            if (card.type == AbstractCard.CardType.ATTACK) {
                this.flash();
                this.addToTop(new ReducePowerAction(this.owner, this.owner, POWER_ID, 1));
                while (energy > 0 && player.hand.size() < 10 && player.discardPile.getAttacks().size() > 0) {
                    player.discardPile.moveToHand(player.discardPile.getRandomCard(CardType.ATTACK, true));
                    energy -= 1;
                }
            }
        }

        @Override
        public void updateDescription() {
            description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        }

        public void stackPower(int stackAmount) {
            this.fontScale = 8.0F;
            this.amount += stackAmount;
            if (this.amount <= 0) {
                AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
            }
        }
    }
}